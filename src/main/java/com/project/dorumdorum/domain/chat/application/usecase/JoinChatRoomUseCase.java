package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageResponse;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoommateAcceptedEvent;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class JoinChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 룸메이트 승인 이벤트 처리
     * - 그룹 채팅방이 없으면 생성하고 방장을 먼저 입장
     * - 승인된 사용자를 중복 없이 입장 처리
     * - 입장 시스템 메시지를 저장하고 실시간 전송
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(RoommateAcceptedEvent event) {
        ChatRoom chatRoom = chatRoomService.findByRoomNo(event.roomNo())
                .orElseGet(() -> {
                    ChatRoom created = chatRoomService.create(event.roomNo());
                    chatRoomMemberService.join(created, event.hostUserNo());
                    return created;
                });

        if (!chatRoomMemberService.isMember(chatRoom, event.acceptedUserNo())) {
            chatRoomMemberService.join(chatRoom, event.acceptedUserNo());
            User accepted = userService.findById(event.acceptedUserNo());
            String displayName = (accepted.getNickname() != null && !accepted.getNickname().isBlank())
                    ? accepted.getNickname() : accepted.getName();
            String content = displayName + "가 입장했습니다.";
            ChatMessage message = chatMessageService.save(chatRoom, "SYSTEM", content, MessageType.SYSTEM, 0);
            ChatMessageResponse response = new ChatMessageResponse(
                    message.getMessageNo(), chatRoom.getChatRoomNo(),
                    "SYSTEM", null, content, MessageType.SYSTEM.name(), message.getCreatedAt());
            messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoom.getChatRoomNo(), response);
        }
    }
}
