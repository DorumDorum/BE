package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.usecase.JoinChatRoomUseCase;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoommateAcceptedEvent;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JoinChatRoomUseCase Unit Tests")
class JoinChatRoomUseCaseTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private UserService userService;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @InjectMocks private JoinChatRoomUseCase useCase;

    private final ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();

    @Test
    @DisplayName("채팅방 없으면 생성 후 방장 + 신규 멤버 입장 및 시스템 메시지 저장")
    void handle_WhenNoChatRoom_CreatesAndJoinsBothMembers() {
        User mockUser = mock(User.class);
        ChatMessage mockMessage = mock(ChatMessage.class);
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.empty());
        when(chatRoomService.create("room-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.isMember(chatRoom, "user-new")).thenReturn(false);
        when(userService.findById("user-new")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("newNick");
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0))).thenReturn(mockMessage);
        when(mockMessage.getMessageNo()).thenReturn("msg-1");
        when(mockMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        useCase.handle(new RoommateAcceptedEvent("room-1", "user-new", "user-host"));

        verify(chatRoomService).create("room-1");
        verify(chatRoomMemberService).join(chatRoom, "user-host");
        verify(chatRoomMemberService).join(chatRoom, "user-new");
        verify(chatMessageService).save(eq(chatRoom), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0));
    }

    @Test
    @DisplayName("채팅방 있고 신규 멤버가 아직 없으면 입장 및 시스템 메시지 저장")
    void handle_WhenChatRoomExists_JoinsNewMemberOnly() {
        User mockUser = mock(User.class);
        ChatMessage mockMessage = mock(ChatMessage.class);
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(chatRoomMemberService.isMember(chatRoom, "user-new")).thenReturn(false);
        when(userService.findById("user-new")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("newNick");
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0))).thenReturn(mockMessage);
        when(mockMessage.getMessageNo()).thenReturn("msg-1");
        when(mockMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        useCase.handle(new RoommateAcceptedEvent("room-1", "user-new", "user-host"));

        verify(chatRoomService, never()).create(any());
        verify(chatRoomMemberService, never()).join(chatRoom, "user-host");
        verify(chatRoomMemberService).join(chatRoom, "user-new");
        verify(chatMessageService).save(eq(chatRoom), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0));
    }

    @Test
    @DisplayName("신규 멤버가 이미 채팅방에 있으면 아무것도 하지 않는다")
    void handle_WhenAlreadyMember_DoesNothing() {
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(chatRoomMemberService.isMember(chatRoom, "user-new")).thenReturn(true);

        useCase.handle(new RoommateAcceptedEvent("room-1", "user-new", "user-host"));

        verify(chatRoomMemberService, never()).join(any(), any());
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
    }
}
