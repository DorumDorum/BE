package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.event.RoommateKickedEventListener;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.room.application.event.RoommateKickedEvent;
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
@DisplayName("RoommateKickedEventListener Unit Tests")
class RoommateKickedEventListenerTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private UserService userService;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @InjectMocks private RoommateKickedEventListener listener;

    private final ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();

    @Test
    @DisplayName("채팅방에 강퇴 멤버가 있으면 퇴장 처리 및 시스템 메시지 저장")
    void handle_WhenMemberExists_LeavesAndSavesSystemMessage() {
        ChatRoomMember member = ChatRoomMember.builder().chatRoom(chatRoom).userNo("user-kicked").build();
        User mockUser = mock(User.class);
        ChatMessage mockMessage = mock(ChatMessage.class);
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(chatRoomMemberService.isMember(chatRoom, "user-kicked")).thenReturn(true);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "user-kicked")).thenReturn(member);
        when(userService.findById("user-kicked")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("kickedNick");
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0))).thenReturn(mockMessage);
        when(mockMessage.getMessageNo()).thenReturn("msg-1");
        when(mockMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

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
    }

    @Test
    @DisplayName("강퇴 유저가 채팅방 멤버가 아니면 아무것도 하지 않는다")
    void handle_WhenNotMember_DoesNothing() {
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(chatRoomMemberService.isMember(chatRoom, "user-kicked")).thenReturn(false);

        listener.handle(new RoommateKickedEvent("room-1", "user-kicked"));

        verify(chatRoomMemberService, never()).leave(any());
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
    }
}
