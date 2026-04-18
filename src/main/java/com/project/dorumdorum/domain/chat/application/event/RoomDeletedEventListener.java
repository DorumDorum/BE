package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.application.dto.response.NotificationMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoomDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomDeletedEventListener {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 방 삭제(RoomDeletedEvent) → GROUP·DIRECT 채팅방 전체 삭제 처리
     * 발행: DeleteRoomUseCase (room 도메인 담당자)
     *
     * BEFORE_COMMIT: 부모 트랜잭션(DeleteRoomUseCase)에 참여하여 방 삭제 + 채팅방 삭제를 하나의 트랜잭션으로 처리.
     * WebSocket 브로드캐스트는 트랜잭션 외부 작업이므로 실패가 TX에 영향을 주지 않도록 독립 처리.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoomDeletedEvent event) {
        List<ChatRoom> chatRooms = chatRoomService.findAllByRoomNo(event.roomNo());
        for (ChatRoom chatRoom : chatRooms) {
            notifyAndClean(chatRoom, event.roomNo());
        }
    }

    private void notifyAndClean(ChatRoom chatRoom, String roomNo) {
        NotificationMessage notification = NotificationMessage.roomDeleted(roomNo, chatRoom.getChatRoomNo());

        // 채팅방 열려 있는 유저에게 브로드캐스트 (/topic/)
        broadcastDeletionSafely(chatRoom, notification);

        // DIRECT 채팅방은 지원자가 다른 화면에 있어도 받을 수 있도록 개인 큐로 추가 전송 (/queue/)
        if (ChatRoomType.DIRECT.equals(chatRoom.getChatRoomType())
                && chatRoom.getApplicantUserNo() != null) {
            notifyUserSafely(chatRoom.getApplicantUserNo(), notification);
        }

        chatMessageService.deleteAllByChatRoom(chatRoom.getChatRoomNo());
        chatRoomMemberService.deleteAllByChatRoom(chatRoom);
        chatRoomService.delete(chatRoom);
    }

    private void broadcastDeletionSafely(ChatRoom chatRoom, NotificationMessage notification) {
        try {
            messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoom.getChatRoomNo(), notification);
        } catch (Exception e) {
            log.warn("[Chat] 방 삭제 WebSocket 브로드캐스트 실패. chatRoomNo={}", chatRoom.getChatRoomNo(), e);
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
