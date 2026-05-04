package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.NotificationMessage;
import com.project.dorumdorum.domain.chat.application.dto.response.NotificationType;
import com.project.dorumdorum.domain.chat.application.event.ChatWebSocketNotificationEvent;
import com.project.dorumdorum.domain.chat.application.event.RoommateKickedEventListener;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoommateKickedEventListener Unit Tests")
class RoommateKickedEventListenerTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private UserService userService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @InjectMocks private RoommateKickedEventListener listener;

    private final ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();

    // ── helpers ──────────────────────────────────────────────────────────

    private ChatRoomMember givenMember(ChatRoom room, String userNo) {
        ChatRoomMember member = ChatRoomMember.builder().chatRoom(room).userNo(userNo).build();
        when(chatRoomMemberService.findOptionalByChatRoomAndUserNo(room, userNo))
                .thenReturn(Optional.of(member));
        return member;
    }

    private ChatMessage givenSystemMessage() {
        ChatMessage msg = mock(ChatMessage.class);
        when(msg.getMessageNo()).thenReturn("msg-1");
        when(msg.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0)))
                .thenReturn(msg);
        return msg;
    }

    // ── tests ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("채팅방에 강퇴 멤버가 있으면 퇴장 처리 및 시스템 메시지 저장")
    void handle_WhenMemberExists_LeavesAndSavesSystemMessage() {
        ChatRoomMember member = givenMember(chatRoom, "user-kicked");
        User mockUser = mock(User.class);
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(userService.findById("user-kicked")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("kickedNick");
        givenSystemMessage();

        listener.handle(new RoommateKickedEvent("room-1", "user-kicked"));

        verify(chatRoomMemberService).leave(member);
        verify(chatMessageService).save(eq(chatRoom), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0));
    }

    @Test
    @DisplayName("채팅방이 없으면 아무것도 하지 않는다")
    void handle_WhenNoChatRoom_DoesNothing() {
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.empty());

        listener.handle(new RoommateKickedEvent("room-1", "user-kicked"));

        verify(chatRoomMemberService, never()).leave(any());
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("강퇴 유저가 채팅방 멤버가 아니면 아무것도 하지 않는다")
    void handle_WhenNotMember_DoesNothing() {
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(chatRoomMemberService.findOptionalByChatRoomAndUserNo(chatRoom, "user-kicked"))
                .thenReturn(Optional.empty());

        listener.handle(new RoommateKickedEvent("room-1", "user-kicked"));

        verify(chatRoomMemberService, never()).leave(any());
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("닉네임이 없으면 이름으로 시스템 메시지 생성")
    void handle_WhenNicknameNull_UsesName() {
        ChatRoomMember member = givenMember(chatRoom, "user-kicked");
        User mockUser = mock(User.class);
        ChatMessage mockMessage = mock(ChatMessage.class);
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(userService.findById("user-kicked")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn(null);
        when(mockUser.getName()).thenReturn("홍길동");
        when(chatMessageService.save(any(), eq("SYSTEM"), contains("홍길동님이"), eq(MessageType.SYSTEM), eq(0)))
                .thenReturn(mockMessage);
        when(mockMessage.getMessageNo()).thenReturn("msg-1");
        when(mockMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        listener.handle(new RoommateKickedEvent("room-1", "user-kicked"));

        verify(chatMessageService).save(eq(chatRoom), eq("SYSTEM"), contains("홍길동님이"), eq(MessageType.SYSTEM), eq(0));
    }

    @Test
    @DisplayName("멤버가 존재하면 decreaseUnreadCount가 leave() 이전에 호출된다")
    void handle_WhenMemberExists_DecreasesUnreadCountBeforeLeave() {
        ChatRoomMember member = givenMember(chatRoom, "user-kicked");
        User mockUser = mock(User.class);
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(userService.findById("user-kicked")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("kickedNick");
        givenSystemMessage();

        listener.handle(new RoommateKickedEvent("room-1", "user-kicked"));

        InOrder inOrder = inOrder(chatMessageService, chatRoomMemberService);
        inOrder.verify(chatMessageService).decreaseUnreadCount(any(), any(), eq("user-kicked"));
        inOrder.verify(chatRoomMemberService).leave(member);
    }

    @Test
    @DisplayName("강퇴 처리 후 이벤트에 브로드캐스트 + 강퇴 사용자 개인 알림 태스크가 담긴다")
    void handle_WhenMemberKicked_PublishesEventWithBroadcastAndUserNotification() {
        givenMember(chatRoom, "user-kicked");
        User mockUser = mock(User.class);
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(userService.findById("user-kicked")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("kickedNick");
        givenSystemMessage();

        listener.handle(new RoommateKickedEvent("room-1", "user-kicked"));

        ArgumentCaptor<ChatWebSocketNotificationEvent> captor =
                ArgumentCaptor.forClass(ChatWebSocketNotificationEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        ChatWebSocketNotificationEvent event = captor.getValue();

        assertThat(event.broadcasts()).hasSize(1);
        assertThat(event.userNotifications()).hasSize(1);
        assertThat(event.userNotifications().get(0).userNo()).isEqualTo("user-kicked");

        NotificationMessage notification = (NotificationMessage) event.userNotifications().get(0).payload();
        assertThat(notification.type()).isEqualTo(NotificationType.KICKED_FROM_ROOM);
        assertThat(notification.roomNo()).isEqualTo("room-1");
    }
}
