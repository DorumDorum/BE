package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.application.dto.response.NotificationMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoomDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomDeletedEventListener {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 방 삭제(RoomDeletedEvent) → GROUP·DIRECT 채팅방 전체 삭제 처리
     * 발행: DeleteRoomUseCase (room 도메인 담당자)
     *
     * BEFORE_COMMIT: 부모 트랜잭션(DeleteRoomUseCase)에 참여하여 방 삭제 + 채팅방 삭제를 하나의 트랜잭션으로 처리.
     * WebSocket 알림 페이로드를 수집한 뒤 ChatWebSocketNotificationEvent로 발행 →
     * AFTER_COMMIT에서 비동기 재처리(exponential backoff)로 전송.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoomDeletedEvent event) {
        List<ChatRoom> chatRooms = chatRoomService.findAllByRoomNo(event.roomNo());

        List<ChatWebSocketNotificationEvent.BroadcastTask> broadcasts = new ArrayList<>();
        List<ChatWebSocketNotificationEvent.UserNotifyTask> userNotifications = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            NotificationMessage notification = NotificationMessage.roomDeleted(event.roomNo(), chatRoom.getChatRoomNo());
            broadcasts.add(new ChatWebSocketNotificationEvent.BroadcastTask(chatRoom.getChatRoomNo(), notification));

            if (ChatRoomType.DIRECT.equals(chatRoom.getChatRoomType())
                    && chatRoom.getApplicantUserNo() != null) {
                userNotifications.add(new ChatWebSocketNotificationEvent.UserNotifyTask(
                        chatRoom.getApplicantUserNo(), notification));
            }

            chatMessageService.deleteAllByChatRoom(chatRoom.getChatRoomNo());
            chatRoomMemberService.deleteAllByChatRoom(chatRoom);
            chatRoomService.deleteByChatRoomNo(chatRoom.getChatRoomNo());
        }

        if (!broadcasts.isEmpty() || !userNotifications.isEmpty()) {
            eventPublisher.publishEvent(new ChatWebSocketNotificationEvent(broadcasts, userNotifications));
        }
    }
}
