package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageResponse;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoommateKickedEvent;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(RoommateKickedEvent event) {
        chatRoomService.findByRoomNo(event.roomNo()).ifPresent(chatRoom -> {
            if (chatRoomMemberService.isMember(chatRoom, event.kickedUserNo())) {
                chatRoomMemberService.leave(
                        chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, event.kickedUserNo())
                );
                User kicked = userService.findById(event.kickedUserNo());
                String displayName = (kicked.getNickname() != null && !kicked.getNickname().isBlank())
                        ? kicked.getNickname() : kicked.getName();
                String content = displayName + "가 퇴장했습니다.";
                ChatMessage message = chatMessageService.save(chatRoom, "SYSTEM", content, MessageType.SYSTEM, 0);
                ChatMessageResponse response = new ChatMessageResponse(
                        message.getMessageNo(), chatRoom.getChatRoomNo(),
                        "SYSTEM", null, content, MessageType.SYSTEM.name(), message.getCreatedAt());
                messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoom.getChatRoomNo(), response);
            }
        });
    }
}
