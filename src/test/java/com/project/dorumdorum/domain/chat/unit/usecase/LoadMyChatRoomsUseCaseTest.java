package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.application.usecase.LoadMyChatRoomsUseCase;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadMyChatRoomsUseCase Unit Tests")
class LoadMyChatRoomsUseCaseTest {

    @Mock private ChatRoomService chatRoomService;
    @InjectMocks private LoadMyChatRoomsUseCase useCase;

    @Test
    @DisplayName("내 채팅방 목록을 반환한다")
    void execute_ReturnsChatRoomList() {
        List<ChatRoomSummary> expected = List.of(
                new ChatRoomSummary("cr-1", "r-1", "마지막 메시지", null, 2L)
        );
        when(chatRoomService.findMyChatRooms("u1")).thenReturn(expected);

        List<ChatRoomSummary> result = useCase.execute("u1");

        verify(chatRoomService).findMyChatRooms("u1");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("참여 중인 채팅방이 없으면 빈 목록을 반환한다")
    void execute_WhenNoChatRooms_ReturnsEmptyList() {
        when(chatRoomService.findMyChatRooms("u1")).thenReturn(List.of());

        List<ChatRoomSummary> result = useCase.execute("u1");

        assertThat(result).isEmpty();
    }
}
