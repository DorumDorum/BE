package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.usecase.SendGroupChatMessageUseCase;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.notification.application.event.NotificationRequestPublisher;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.ChatErrorStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendGroupChatMessageUseCase Unit Tests")
class SendGroupChatMessageUseCaseTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @Mock private NotificationRequestPublisher notificationRequestPublisher;
    @Mock private UserService userService;
    @InjectMocks private SendGroupChatMessageUseCase useCase;

    @Test
    @DisplayName("메시지 저장 후 broadcast하고 비발신자에게 FCM 알림을 전송한다")
    void send_SavesBroadcastsAndNotifiesOthers() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatMessage message = mock(ChatMessage.class);
        ChatRoomMember sender = mock(ChatRoomMember.class);
        ChatRoomMember other = mock(ChatRoomMember.class);

        User mockUser = mock(User.class);
        when(chatRoom.getChatRoomNo()).thenReturn("cr-1");
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(sender.getUserNo()).thenReturn("user-1");
        when(other.getUserNo()).thenReturn("user-2");
        when(chatRoomMemberService.findByChatRoom(chatRoom)).thenReturn(List.of(sender, other));
        when(chatMessageService.save(chatRoom, "user-1", "hello", MessageType.TEXT, 1)).thenReturn(message);
        when(message.getMessageNo()).thenReturn("msg-1");
        when(message.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(userService.findById("user-1")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("user1Nick");

        useCase.send("cr-1", "user-1", "hello");

        verify(chatRoomMemberService, never()).validateMembership(any(), any());
        verify(chatMessageService).save(chatRoom, "user-1", "hello", MessageType.TEXT, 1);
        verify(chatRoomService).updateLastMessage(eq(chatRoom), eq("hello"), eq("user-1"), any());
        verify(messagingTemplate).convertAndSend(eq("/topic/chat-room/cr-1"), any(Object.class));
        verify(notificationRequestPublisher).publish(
                eq("user-2"), anyString(), eq("hello"),
                eq(NotificationType.NEW_MESSAGE_RECEIVED), eq("cr-1"));
        verify(notificationRequestPublisher, never()).publish(
                eq("user-1"), any(), any(), any(), any());
    }

    @Test
    @DisplayName("멤버가 한 명이면 unreadCount 0으로 저장하고 FCM 알림을 보내지 않는다")
    void send_WhenOnlyOneMember_SavesWithZeroUnreadAndNoNotification() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatMessage message = mock(ChatMessage.class);
        ChatRoomMember sender = mock(ChatRoomMember.class);

        User mockUser = mock(User.class);
        when(chatRoom.getChatRoomNo()).thenReturn("cr-1");
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(sender.getUserNo()).thenReturn("user-1");
        when(chatRoomMemberService.findByChatRoom(chatRoom)).thenReturn(List.of(sender));
        when(chatMessageService.save(chatRoom, "user-1", "hello", MessageType.TEXT, 0)).thenReturn(message);
        when(message.getMessageNo()).thenReturn("msg-1");
        when(message.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(userService.findById("user-1")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("user1Nick");

        useCase.send("cr-1", "user-1", "hello");

        verify(chatMessageService).save(chatRoom, "user-1", "hello", MessageType.TEXT, 0);
        verify(notificationRequestPublisher, never()).publish(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("채팅방 멤버가 아닌 사용자가 메시지 전송 시 validateMembership을 호출한다")
    void send_WhenNotMember_CallsValidateMembership() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember other = mock(ChatRoomMember.class);

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(other.getUserNo()).thenReturn("user-2");
        when(chatRoomMemberService.findByChatRoom(chatRoom)).thenReturn(List.of(other));
        doThrow(new RestApiException(ChatErrorStatus.NOT_CHAT_ROOM_MEMBER))
                .when(chatRoomMemberService).validateMembership(chatRoom, "user-1");

        assertThatThrownBy(() -> useCase.send("cr-1", "user-1", "hello"))
                .isInstanceOf(RestApiException.class);

        verify(chatRoomMemberService).validateMembership(chatRoom, "user-1");
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
    }

    @Test
    @DisplayName("nickname이 null이면 name으로 대체하여 메시지를 전송한다")
    void send_WhenNicknameIsNull_UsesNameAsFallback() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatMessage message = mock(ChatMessage.class);
        ChatRoomMember sender = mock(ChatRoomMember.class);
        ChatRoomMember other = mock(ChatRoomMember.class);
        User mockUser = mock(User.class);

        when(chatRoom.getChatRoomNo()).thenReturn("cr-1");
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(sender.getUserNo()).thenReturn("user-1");
        when(other.getUserNo()).thenReturn("user-2");
        when(chatRoomMemberService.findByChatRoom(chatRoom)).thenReturn(List.of(sender, other));
        when(chatMessageService.save(chatRoom, "user-1", "hi", MessageType.TEXT, 1)).thenReturn(message);
        when(message.getMessageNo()).thenReturn("msg-1");
        when(message.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(userService.findById("user-1")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn(null);
        when(mockUser.getName()).thenReturn("홍길동");

        useCase.send("cr-1", "user-1", "hi");

        verify(notificationRequestPublisher).publish(
                eq("user-2"), eq("홍길동"), eq("hi"),
                eq(NotificationType.NEW_MESSAGE_RECEIVED), eq("cr-1"));
    }

    @Test
    @DisplayName("메시지 저장 중 예외 발생 시 broadcast와 알림이 발행되지 않는다")
    void send_WhenSaveThrows_DoesNotBroadcastOrNotify() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember sender = mock(ChatRoomMember.class);

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(sender.getUserNo()).thenReturn("user-1");
        when(chatRoomMemberService.findByChatRoom(chatRoom)).thenReturn(List.of(sender));
        when(chatMessageService.save(any(), any(), any(), any(), anyInt()))
                .thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> useCase.send("cr-1", "user-1", "hello"))
                .isInstanceOf(RuntimeException.class);

        verify(messagingTemplate, never()).convertAndSend(anyString(), any(Object.class));
        verify(notificationRequestPublisher, never()).publish(any(), any(), any(), any(), any());
    }
}
