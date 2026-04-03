package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.usecase.LeaveChatRoomUseCase;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
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

import org.mockito.InOrder;

@ExtendWith(MockitoExtension.class)
@DisplayName("LeaveChatRoomUseCase Unit Tests")
class LeaveChatRoomUseCaseTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @Mock private UserService userService;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @InjectMocks private LeaveChatRoomUseCase useCase;

    @Test
    @DisplayName("GROUP 채팅방에서 비방장 멤버는 퇴장할 수 있고 시스템 메시지가 저장된다")
    void execute_WhenNonHost_LeavesAndSavesSystemMessage() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        Room room = mock(Room.class);

        User mockUser = mock(User.class);
        ChatMessage mockMessage = mock(ChatMessage.class);
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(2L);
        when(chatRoom.getRoomNo()).thenReturn("room-1");
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.GROUP);
        when(roomService.findByIdForUpdate("room-1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(false);
        when(userService.findById("u1")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("nick1");
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0))).thenReturn(mockMessage);
        when(mockMessage.getMessageNo()).thenReturn("msg-1");
        when(mockMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        useCase.execute("cr-1", "u1");

        verify(chatRoomMemberService).leave(member);
        verify(roommateService).leaveRoom("u1", "room-1");
        verify(room).minusCurrentMate();
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
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.GROUP);
        when(roomService.findByIdForUpdate("room-1")).thenReturn(mock(Room.class));
        when(roommateService.isHost(eq("u1"), any(Room.class))).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute("cr-1", "u1"))
                .isInstanceOf(RestApiException.class);

        verify(chatRoomMemberService, never()).leave(any());
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
    }

    @Test
    @DisplayName("GROUP 채팅방에서 마지막 멤버가 퇴장하면 채팅방이 삭제되고 방 인원수가 감소한다")
    void execute_WhenLastMember_LeavesAndDeletesChatRoom() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        Room room = mock(Room.class);

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(1L);
        when(chatRoom.getRoomNo()).thenReturn("room-1");
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.GROUP);
        when(roomService.findByIdForUpdate("room-1")).thenReturn(room);

        useCase.execute("cr-1", "u1");

        verify(chatRoomMemberService).leave(member);
        verify(roommateService).leaveRoom("u1", "room-1");
        verify(room).minusCurrentMate();
        verify(chatRoomService).delete(chatRoom);
        verify(chatMessageService, never()).save(any(), any(), any(), any(), anyInt());
    }

    @Test
    @DisplayName("비방장이 퇴장할 때 lastReadAt이 없으면 joinedAt 기준으로 decreaseUnreadCount가 leave() 이전에 호출된다")
    void execute_WhenNonHostLeaves_DecreasesUnreadCountBeforeLeave() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        Room room = mock(Room.class);
        User mockUser = mock(User.class);
        ChatMessage mockMessage = mock(ChatMessage.class);

        LocalDateTime joinedAt = LocalDateTime.of(2026, 1, 1, 0, 0);
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(2L);
        when(chatRoom.getRoomNo()).thenReturn("room-1");
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.GROUP);
        when(chatRoom.getChatRoomNo()).thenReturn("cr-1");
        when(roomService.findByIdForUpdate("room-1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(false);
        when(member.getLastReadAt()).thenReturn(null);
        when(member.getJoinedAt()).thenReturn(joinedAt);
        when(userService.findById("u1")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("nick1");
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0))).thenReturn(mockMessage);
        when(mockMessage.getMessageNo()).thenReturn("msg-1");
        when(mockMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        useCase.execute("cr-1", "u1");

        verify(chatMessageService).decreaseUnreadCount("cr-1", LocalDateTime.of(2026, 1, 1, 0, 0), "u1");

        InOrder inOrder = inOrder(chatMessageService, chatRoomMemberService);
        inOrder.verify(chatMessageService).decreaseUnreadCount("cr-1", LocalDateTime.of(2026, 1, 1, 0, 0), "u1");
        inOrder.verify(chatRoomMemberService).leave(member);
    }

    @Test
    @DisplayName("마지막 멤버가 퇴장할 때는 decreaseUnreadCount가 호출되지 않는다")
    void execute_WhenLastMemberLeaves_DoesNotDecreaseUnreadCount() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        Room room = mock(Room.class);

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(1L);
        when(chatRoom.getRoomNo()).thenReturn("room-1");
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.GROUP);
        when(roomService.findByIdForUpdate("room-1")).thenReturn(room);

        useCase.execute("cr-1", "u1");

        verify(chatMessageService, never()).decreaseUnreadCount(any(), any(), any());
    }

    @Test
    @DisplayName("lastReadAt이 있으면 lastReadAt을 fromTime으로 decreaseUnreadCount가 호출된다")
    void execute_WhenLastReadAtExists_UsesLastReadAtAsFromTime() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        Room room = mock(Room.class);
        User mockUser = mock(User.class);
        ChatMessage mockMessage = mock(ChatMessage.class);

        LocalDateTime lastReadAt = LocalDateTime.of(2026, 3, 1, 12, 0);
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(2L);
        when(chatRoom.getRoomNo()).thenReturn("room-1");
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.GROUP);
        when(chatRoom.getChatRoomNo()).thenReturn("cr-1");
        when(roomService.findByIdForUpdate("room-1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(false);
        when(member.getLastReadAt()).thenReturn(lastReadAt);
        when(userService.findById("u1")).thenReturn(mockUser);
        when(mockUser.getNickname()).thenReturn("nick1");
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0))).thenReturn(mockMessage);
        when(mockMessage.getMessageNo()).thenReturn("msg-1");
        when(mockMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        useCase.execute("cr-1", "u1");

        verify(chatMessageService).decreaseUnreadCount("cr-1", LocalDateTime.of(2026, 3, 1, 12, 0), "u1");
    }

    @Test
    @DisplayName("DIRECT 채팅방 퇴장 시 룸메이트 삭제와 방 인원수 감소가 일어나지 않는다")
    void execute_WhenDirectChatRoom_DoesNotUpdateRoomCount() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(1L);
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.DIRECT);

        useCase.execute("cr-1", "u1");

        verify(chatRoomMemberService).leave(member);
        verify(roommateService, never()).leaveRoom(any(), any());
        verify(roomService, never()).findByIdForUpdate(any());
        verify(chatRoomService).delete(chatRoom);
    }

    @Test
    @DisplayName("DIRECT 채팅방에서 다른 멤버가 있을 때는 unreadCount를 감소시키고 나간다")
    void execute_WhenDirectChatHasOtherMember_DecreasesUnreadCountBeforeLeave() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        User user = mock(User.class);
        ChatMessage message = mock(ChatMessage.class);

        LocalDateTime lastReadAt = LocalDateTime.of(2026, 5, 1, 10, 0);
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(2L);
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.DIRECT);
        when(chatRoom.getChatRoomNo()).thenReturn("cr-1");
        when(member.getLastReadAt()).thenReturn(lastReadAt);
        when(userService.findById("u1")).thenReturn(user);
        when(user.getNickname()).thenReturn("nick");
        when(chatMessageService.save(any(), eq("SYSTEM"), anyString(), eq(MessageType.SYSTEM), eq(0))).thenReturn(message);
        when(message.getMessageNo()).thenReturn("msg-1");
        when(message.getCreatedAt()).thenReturn(LocalDateTime.now());

        useCase.execute("cr-1", "u1");

        verify(chatMessageService).decreaseUnreadCount("cr-1", lastReadAt, "u1");
        InOrder inOrder = inOrder(chatMessageService, chatRoomMemberService);
        inOrder.verify(chatMessageService).decreaseUnreadCount("cr-1", lastReadAt, "u1");
        inOrder.verify(chatRoomMemberService).leave(member);
    }

    @Test
    @DisplayName("DIRECT 채팅방에서 방장은 다른 멤버가 있으면 퇴장할 수 없다")
    void execute_WhenDirectHostHasOtherMember_ThrowsHostCannotLeave() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(chatRoomMemberService.countByChatRoom(chatRoom)).thenReturn(2L);
        when(chatRoom.getChatRoomType()).thenReturn(ChatRoomType.DIRECT);
        when(chatRoom.getRoomNo()).thenReturn("room-1");
        when(roommateService.isHostOfRoom("u1", "room-1")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute("cr-1", "u1"))
                .isInstanceOf(RestApiException.class);

        verify(chatMessageService, never()).decreaseUnreadCount(any(), any(), any());
        verify(chatRoomMemberService, never()).leave(any());
    }
}
