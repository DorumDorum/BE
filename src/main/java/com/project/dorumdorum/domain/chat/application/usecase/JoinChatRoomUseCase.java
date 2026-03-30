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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 방장이 룸메이트 승인(RoommateAcceptedEvent) → 채팅방 입장
     * - 채팅방 없으면 생성 후 방장 + 신규 멤버 입장
     * - 채팅방 있으면 신규 멤버만 입장 (중복 방지)
     * - 입장 시스템 메시지 저장
     *
     * BEFORE_COMMIT: 부모 트랜잭션(DecideApplicationRequestUseCase.approve)에 참여.
     * 채팅방 입장 실패 시 룸메이트 승인도 롤백 → 원자성 보장.
     * WebSocket 브로드캐스트는 트랜잭션 외부 작업이므로 실패가 TX에 영향을 주지 않도록 독립 처리.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoommateAcceptedEvent event) {
        ChatRoom chatRoom = chatRoomService.findByRoomNo(event.roomNo())
                .orElseGet(() -> {
                    ChatRoom created = chatRoomService.create(event.roomNo());
                    try {
                        chatRoomMemberService.join(created, event.hostUserNo());
                    } catch (DataIntegrityViolationException e) {
                        log.debug("[Chat] 방장 중복 입장 (동시 요청). roomNo={}, hostUserNo={}",
                                event.roomNo(), event.hostUserNo());
                    }
                    return created;
                });

        if (!chatRoomMemberService.isMember(chatRoom, event.acceptedUserNo())) {
            try {
                chatRoomMemberService.join(chatRoom, event.acceptedUserNo());
            } catch (DataIntegrityViolationException e) {
                log.debug("[Chat] 신규 멤버 중복 입장 (동시 요청). roomNo={}, userNo={}",
                        event.roomNo(), event.acceptedUserNo());
                return;
            }
            User accepted = userService.findById(event.acceptedUserNo());
            String displayName = (accepted.getNickname() != null && !accepted.getNickname().isBlank())
                    ? accepted.getNickname() : accepted.getName();
            String content = displayName + "가 입장했습니다.";
            ChatMessage message = chatMessageService.save(chatRoom, "SYSTEM", content, MessageType.SYSTEM, 0);
            ChatMessageResponse response = new ChatMessageResponse(
                    message.getMessageNo(), chatRoom.getChatRoomNo(),
                    "SYSTEM", null, content, MessageType.SYSTEM.name(), message.getCreatedAt());
            broadcastSafely(chatRoom, response);
        }
    }

    private void broadcastSafely(ChatRoom chatRoom, ChatMessageResponse response) {
        try {
            messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoom.getChatRoomNo(), response);
        } catch (Exception e) {
            log.warn("[Chat] WebSocket 브로드캐스트 실패. chatRoomNo={}", chatRoom.getChatRoomNo(), e);
        }
    }
}
