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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoommateKickedEventListener {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 룸메이트 강퇴(RoommateKickedEvent) → 채팅방에서 퇴장 처리
     * 발행: KickRoommateUseCase (room 도메인 담당자)
     *
     * BEFORE_COMMIT: 부모 트랜잭션(KickRoommateUseCase)에 참여하여 방 강퇴 + 채팅방 퇴장을 하나의 트랜잭션으로 처리.
     * 채팅방 퇴장 실패 시 방 강퇴도 롤백되어 데이터 정합성 보장.
     * WebSocket 알림 페이로드를 수집한 뒤 ChatWebSocketNotificationEvent로 발행 →
     * AFTER_COMMIT에서 비동기 재처리(exponential backoff)로 전송.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoommateKickedEvent event) {
        chatRoomService.findByRoomNo(event.roomNo()).ifPresent(chatRoom ->
                chatRoomMemberService.findOptionalByChatRoomAndUserNo(chatRoom, event.kickedUserNo())
                        .ifPresent(member -> processKick(chatRoom, member, event))
        );
    }

    private void processKick(ChatRoom chatRoom, ChatRoomMember member, RoommateKickedEvent event) {
        LocalDateTime fromTime = member.getLastReadAt() != null
                ? member.getLastReadAt()
                : member.getJoinedAt();
        chatMessageService.decreaseUnreadCount(chatRoom.getChatRoomNo(), fromTime, event.kickedUserNo());
        chatRoomMemberService.leave(member);

        User kicked = userService.findById(event.kickedUserNo());
        String displayName = (kicked.getNickname() != null && !kicked.getNickname().isBlank())
                ? kicked.getNickname() : kicked.getName();
        String content = displayName + "님이 퇴장했습니다.";
        ChatMessage message = chatMessageService.save(chatRoom, "SYSTEM", content, MessageType.SYSTEM, 0);

        ChatMessageResponse broadcastPayload = new ChatMessageResponse(
                message.getMessageNo(), chatRoom.getChatRoomNo(),
                "SYSTEM", null, content, MessageType.SYSTEM.name(), message.getCreatedAt(), message.getUnreadCount());
        NotificationMessage notificationPayload = NotificationMessage.kicked(event.roomNo(), chatRoom.getChatRoomNo());

        eventPublisher.publishEvent(new ChatWebSocketNotificationEvent(
                List.of(new ChatWebSocketNotificationEvent.BroadcastTask(chatRoom.getChatRoomNo(), broadcastPayload)),
                List.of(new ChatWebSocketNotificationEvent.UserNotifyTask(event.kickedUserNo(), notificationPayload))
        ));
    }
}
