package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.NotificationMessage;
import com.project.dorumdorum.domain.chat.application.dto.response.NotificationType;
import com.project.dorumdorum.domain.chat.application.event.ChatWebSocketNotificationEvent;
import com.project.dorumdorum.domain.chat.application.event.RoomDeletedEventListener;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
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

    private ChatRoomMember memberOf(ChatRoom chatRoom, String userNo) {
        return ChatRoomMember.builder().chatRoom(chatRoom).userNo(userNo).build();
    }

    @Test
    @DisplayName("채팅방 멤버 전원에게 /queue/notification 개인 알림이 발행된다")
    void handle_NotifiesAllMembersViaUserNotification() {
        ChatRoom room = ChatRoom.builder().chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP).build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(room));
        when(chatRoomMemberService.findByChatRoom(room))
                .thenReturn(List.of(memberOf(room, "user-host")));

        listener.handle(new RoomDeletedEvent("r1"));

        ArgumentCaptor<ChatWebSocketNotificationEvent> captor =
                ArgumentCaptor.forClass(ChatWebSocketNotificationEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        ChatWebSocketNotificationEvent event = captor.getValue();

        assertThat(event.broadcasts()).isEmpty();
        assertThat(event.userNotifications()).hasSize(1);
        assertThat(event.userNotifications().get(0).userNo()).isEqualTo("user-host");

        NotificationMessage notification = (NotificationMessage) event.userNotifications().get(0).payload();
        assertThat(notification.type()).isEqualTo(NotificationType.ROOM_DELETED);
        assertThat(notification.roomNo()).isEqualTo("r1");
        assertThat(notification.chatRoomNo()).isEqualTo("cr-1");
    }

    @Test
    @DisplayName("DIRECT 채팅방 — 멤버 전원(host + applicant)에게 개인 알림")
    void handle_DirectChatRoom_NotifiesBothMembers() {
        ChatRoom directRoom = ChatRoom.builder().chatRoomNo("cr-2").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT).applicantUserNo("applicant-1").build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(directRoom));
        when(chatRoomMemberService.findByChatRoom(directRoom))
                .thenReturn(List.of(memberOf(directRoom, "user-host"), memberOf(directRoom, "applicant-1")));

        listener.handle(new RoomDeletedEvent("r1"));

        ArgumentCaptor<ChatWebSocketNotificationEvent> captor =
                ArgumentCaptor.forClass(ChatWebSocketNotificationEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        ChatWebSocketNotificationEvent event = captor.getValue();

        assertThat(event.broadcasts()).isEmpty();
        assertThat(event.userNotifications()).hasSize(2);
        assertThat(event.userNotifications())
                .extracting(ChatWebSocketNotificationEvent.UserNotifyTask::userNo)
                .containsExactlyInAnyOrder("user-host", "applicant-1");
    }

    @Test
    @DisplayName("멤버가 없는 채팅방이면 이벤트를 발행하지 않는다")
    void handle_NoMembers_DoesNotPublishEvent() {
        ChatRoom room = ChatRoom.builder().chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP).build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(room));
        when(chatRoomMemberService.findByChatRoom(room)).thenReturn(List.of());

        listener.handle(new RoomDeletedEvent("r1"));

        verify(eventPublisher, never()).publishEvent(any());
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
    @DisplayName("메시지·멤버·채팅방 순서로 삭제된다")
    void handle_DeletesInOrder() {
        ChatRoom room = ChatRoom.builder().chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP).build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(room));
        when(chatRoomMemberService.findByChatRoom(room))
                .thenReturn(List.of(memberOf(room, "user-host")));

        listener.handle(new RoomDeletedEvent("r1"));

        InOrder inOrder = inOrder(chatMessageService, chatRoomMemberService, chatRoomService);
        inOrder.verify(chatMessageService).deleteAllByChatRoom("cr-1");
        inOrder.verify(chatRoomMemberService).deleteAllByChatRoom(room);
        inOrder.verify(chatRoomService).deleteByChatRoomNo("cr-1");
    }

    @Test
    @DisplayName("복수 채팅방이 있으면 각 채팅방 멤버 모두 알림 수신")
    void handle_MultipleRooms_AllMembersNotified() {
        ChatRoom groupRoom = ChatRoom.builder().chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP).build();
        ChatRoom directRoom = ChatRoom.builder().chatRoomNo("cr-2").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT).build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(groupRoom, directRoom));
        when(chatRoomMemberService.findByChatRoom(groupRoom))
                .thenReturn(List.of(memberOf(groupRoom, "user-host")));
        when(chatRoomMemberService.findByChatRoom(directRoom))
                .thenReturn(List.of(memberOf(directRoom, "user-host"), memberOf(directRoom, "applicant-1")));

        listener.handle(new RoomDeletedEvent("r1"));

        verify(chatRoomService).deleteByChatRoomNo("cr-1");
        verify(chatRoomService).deleteByChatRoomNo("cr-2");

        ArgumentCaptor<ChatWebSocketNotificationEvent> captor =
                ArgumentCaptor.forClass(ChatWebSocketNotificationEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        ChatWebSocketNotificationEvent event = captor.getValue();
        assertThat(event.broadcasts()).isEmpty();
        assertThat(event.userNotifications()).hasSize(3);
    }
}
