package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.usecase.LeaveChatRoomUseCase;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LeaveChatRoomUseCase Unit Tests")
class LeaveChatRoomUseCaseTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private RoommateService roommateService;
    @Mock private UserService userService;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @InjectMocks private LeaveChatRoomUseCase useCase;

    @Test
    @DisplayName("비방장 멤버는 퇴장할 수 있고 시스템 메시지가 저장된다")
    void execute_WhenNonHost_LeavesAndSavesSystemMessage() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);

        User mockUser = mock(User.class);
        ChatMessage mockMessage = mock(ChatMessage.class);
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(2L);
        when(chatRoom.getRoomNo()).thenReturn("room-1");
        when(roommateService.isHostOfRoom("u1", "room-1")).thenReturn(false);
        when(userService.findById("u1")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("nick1");
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0))).thenReturn(mockMessage);
        when(mockMessage.getMessageNo()).thenReturn("msg-1");
        when(mockMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        useCase.execute("cr-1", "u1");

        verify(chatRoomMemberService).leave(member);
        verify(roommateService).leaveRoom("u1", "room-1");
        verify(chatMessageService).save(eq(chatRoom), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0));
        verify(chatRoomService, never()).delete(any());
    }

    @Test
    @DisplayName("다른 멤버가 있는 경우 방장은 퇴장할 수 없다")
    void execute_WhenHostWithOtherMembers_ThrowsHostCannotLeave() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(2L);
        when(chatRoom.getRoomNo()).thenReturn("room-1");
        when(roommateService.isHostOfRoom("u1", "room-1")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute("cr-1", "u1"))
                .isInstanceOf(RestApiException.class);

        verify(chatRoomMemberService, never()).leave(any());
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
    }

    @Test
    @DisplayName("마지막 멤버가 퇴장하면 채팅방이 삭제되고 시스템 메시지는 저장되지 않는다")
    void execute_WhenLastMember_LeavesAndDeletesChatRoom() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(1L);
        when(chatRoom.getRoomNo()).thenReturn("room-1");

        useCase.execute("cr-1", "u1");

        verify(chatRoomMemberService).leave(member);
        verify(roommateService).leaveRoom("u1", "room-1");
        verify(chatRoomService).delete(chatRoom);
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
    }
}
