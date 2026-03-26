package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.usecase.CreateChatRoomUseCase;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoomConfirmedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateChatRoomUseCase Unit Tests")
class CreateChatRoomUseCaseTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @InjectMocks private CreateChatRoomUseCase useCase;

    @Test
    @DisplayName("채팅방이 없으면 생성 후 전원 입장")
    void handle_WhenNotExists_CreatesChatRoomAndJoinsAll() {
        ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.empty());
        when(chatRoomService.create("room-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.isMember(chatRoom, "user-1")).thenReturn(false);
        when(chatRoomMemberService.isMember(chatRoom, "user-2")).thenReturn(false);

        useCase.handle(new RoomConfirmedEvent("room-1", List.of("user-1", "user-2")));

        verify(chatRoomService).create("room-1");
        verify(chatRoomMemberService).join(chatRoom, "user-1");
        verify(chatRoomMemberService).join(chatRoom, "user-2");
    }

    @Test
    @DisplayName("채팅방이 이미 있으면 미입장 멤버만 추가하고 기존 멤버는 스킵")
    void handle_WhenAlreadyExists_JoinsOnlyNonMembers() {
        ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();
        when(chatRoomService.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));
        when(chatRoomMemberService.isMember(chatRoom, "user-1")).thenReturn(true);
        when(chatRoomMemberService.isMember(chatRoom, "user-2")).thenReturn(false);

        useCase.handle(new RoomConfirmedEvent("room-1", List.of("user-1", "user-2")));

        verify(chatRoomService, never()).create(any());
        verify(chatRoomMemberService, never()).join(chatRoom, "user-1");
        verify(chatRoomMemberService).join(chatRoom, "user-2");
    }
}
