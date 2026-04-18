package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageResponse;
import com.project.dorumdorum.domain.chat.application.dto.response.NotificationMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoommateKickedEvent;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoommateKickedEventListener {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 룸메이트 강퇴(RoommateKickedEvent) → 채팅방에서 퇴장 처리
     * 발행: KickRoommateUseCase (room 도메인 담당자)
     *
     * BEFORE_COMMIT: 부모 트랜잭션(KickRoommateUseCase)에 참여하여 방 강퇴 + 채팅방 퇴장을 하나의 트랜잭션으로 처리.
     * 채팅방 퇴장 실패 시 방 강퇴도 롤백되어 데이터 정합성 보장.
     * WebSocket 브로드캐스트는 트랜잭션 외부 작업이므로 실패가 TX에 영향을 주지 않도록 독립 처리.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoommateKickedEvent event) {
        chatRoomService.findByRoomNo(event.roomNo()).ifPresent(chatRoom -> {
            if (chatRoomMemberService.isMember(chatRoom, event.kickedUserNo())) {
                ChatRoomMember member = chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, event.kickedUserNo());
                LocalDateTime fromTime = member.getLastReadAt() != null
                        ? member.getLastReadAt()
                        : member.getJoinedAt();
                chatMessageService.decreaseUnreadCount(chatRoom.getChatRoomNo(), fromTime, event.kickedUserNo());
                chatRoomMemberService.leave(member);
                User kicked = userService.findById(event.kickedUserNo());
                String displayName = (kicked.getNickname() != null && !kicked.getNickname().isBlank())
                        ? kicked.getNickname() : kicked.getName();
                String content = displayName + "가 퇴장했습니다.";
                ChatMessage message = chatMessageService.save(chatRoom, "SYSTEM", content, MessageType.SYSTEM, 0);
                ChatMessageResponse response = new ChatMessageResponse(
                        message.getMessageNo(), chatRoom.getChatRoomNo(),
                        "SYSTEM", null, content, MessageType.SYSTEM.name(), message.getCreatedAt(), message.getUnreadCount());
                broadcastSafely(chatRoom, response);
                notifyUserSafely(event.kickedUserNo(),
                        NotificationMessage.kicked(event.roomNo(), chatRoom.getChatRoomNo()));
            }
        });
    }

    private void broadcastSafely(ChatRoom chatRoom, ChatMessageResponse response) {
        try {
            messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoom.getChatRoomNo(), response);
        } catch (Exception e) {
            log.warn("[Chat] WebSocket 브로드캐스트 실패. chatRoomNo={}", chatRoom.getChatRoomNo(), e);
        }
    }

    private void notifyUserSafely(String userNo, NotificationMessage notification) {
        try {
            messagingTemplate.convertAndSendToUser(userNo, "/queue/notification", notification);
        } catch (Exception e) {
            log.warn("[Chat] 개인 알림 전송 실패. userNo={}", userNo, e);
        }
    }
}
