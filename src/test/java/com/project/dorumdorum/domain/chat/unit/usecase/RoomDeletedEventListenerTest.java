package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.NotificationMessage;
import com.project.dorumdorum.domain.chat.application.dto.response.NotificationType;
import com.project.dorumdorum.domain.chat.application.event.ChatWebSocketNotificationEvent;
import com.project.dorumdorum.domain.chat.application.event.RoomDeletedEventListener;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoomDeletedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomDeletedEventListener Unit Tests")
class RoomDeletedEventListenerTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @InjectMocks private RoomDeletedEventListener listener;

    @Test
    @DisplayName("GROUP 채팅방 — 브로드캐스트 이벤트 발행 후 메시지·멤버·채팅방 순서로 삭제")
    void handle_GroupChatRoom_PublishesEventAndDeletesInOrder() {
        ChatRoom groupRoom = ChatRoom.builder()
                .chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP)
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(groupRoom));

        listener.handle(new RoomDeletedEvent("r1"));

        // DB 삭제 순서 검증
        InOrder inOrder = inOrder(chatMessageService, chatRoomMemberService, chatRoomService);
        inOrder.verify(chatMessageService).deleteAllByChatRoom("cr-1");
        inOrder.verify(chatRoomMemberService).deleteAllByChatRoom(groupRoom);
        inOrder.verify(chatRoomService).deleteByChatRoomNo("cr-1");

        // 발행된 이벤트에 GROUP 브로드캐스트 태스크만 있고 개인 알림은 없는지 검증
        ArgumentCaptor<ChatWebSocketNotificationEvent> captor =
                ArgumentCaptor.forClass(ChatWebSocketNotificationEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        ChatWebSocketNotificationEvent event = captor.getValue();
        assertThat(event.broadcasts()).hasSize(1);
        assertThat(event.broadcasts().get(0).chatRoomNo()).isEqualTo("cr-1");
        assertThat(event.userNotifications()).isEmpty();
    }

    @Test
    @DisplayName("DIRECT 채팅방 — 브로드캐스트 이벤트 + 지원자 개인 알림 태스크 포함")
    void handle_DirectChatRoom_PublishesEventWithUserNotification() {
        ChatRoom directRoom = ChatRoom.builder()
                .chatRoomNo("cr-2").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo("applicant-1")
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(directRoom));

        listener.handle(new RoomDeletedEvent("r1"));

        ArgumentCaptor<ChatWebSocketNotificationEvent> captor =
                ArgumentCaptor.forClass(ChatWebSocketNotificationEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        ChatWebSocketNotificationEvent event = captor.getValue();

        assertThat(event.broadcasts()).hasSize(1);
        assertThat(event.broadcasts().get(0).chatRoomNo()).isEqualTo("cr-2");
        assertThat(event.userNotifications()).hasSize(1);
        assertThat(event.userNotifications().get(0).userNo()).isEqualTo("applicant-1");

        NotificationMessage notification = (NotificationMessage) event.userNotifications().get(0).payload();
        assertThat(notification.type()).isEqualTo(NotificationType.ROOM_DELETED);
        assertThat(notification.roomNo()).isEqualTo("r1");
    }

    @Test
    @DisplayName("DIRECT 채팅방에서 applicantUserNo가 null이면 개인 알림 태스크 없음")
    void handle_DirectChatRoomWithNullApplicant_NoUserNotificationTask() {
        ChatRoom directRoom = ChatRoom.builder()
                .chatRoomNo("cr-3").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo(null)
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(directRoom));

        listener.handle(new RoomDeletedEvent("r1"));

        ArgumentCaptor<ChatWebSocketNotificationEvent> captor =
                ArgumentCaptor.forClass(ChatWebSocketNotificationEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().userNotifications()).isEmpty();
    }

    @Test
    @DisplayName("채팅방이 없으면 DB 삭제 및 이벤트 발행 없이 종료")
    void handle_NoChatRooms_DoesNothing() {
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of());

        listener.handle(new RoomDeletedEvent("r1"));

        verify(chatMessageService, never()).deleteAllByChatRoom(any());
        verify(chatRoomMemberService, never()).deleteAllByChatRoom(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("복수 채팅방(GROUP + DIRECT)이 있으면 이벤트에 모두 포함")
    void handle_MultipleRooms_EventContainsAllTasks() {
        ChatRoom groupRoom = ChatRoom.builder()
                .chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP)
                .build();
        ChatRoom directRoom = ChatRoom.builder()
                .chatRoomNo("cr-2").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo("applicant-1")
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(groupRoom, directRoom));

        listener.handle(new RoomDeletedEvent("r1"));

        verify(chatRoomService).deleteByChatRoomNo("cr-1");
        verify(chatRoomService).deleteByChatRoomNo("cr-2");

        ArgumentCaptor<ChatWebSocketNotificationEvent> captor =
                ArgumentCaptor.forClass(ChatWebSocketNotificationEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        ChatWebSocketNotificationEvent event = captor.getValue();
        assertThat(event.broadcasts()).hasSize(2);
        assertThat(event.userNotifications()).hasSize(1);
        assertThat(event.userNotifications().get(0).userNo()).isEqualTo("applicant-1");
    }
}
