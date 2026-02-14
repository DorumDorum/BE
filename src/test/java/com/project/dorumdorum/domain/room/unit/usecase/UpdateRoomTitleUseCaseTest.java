package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomTitleRequest;
import com.project.dorumdorum.domain.room.application.usecase.UpdateRoomTitleUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateRoomTitleUseCase Unit Tests")
class UpdateRoomTitleUseCaseTest {

    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @InjectMocks private UpdateRoomTitleUseCase useCase;

    @Test
    @DisplayName("Should trim and update title when user is host")
    void execute_WhenHost_UpdatesTitle() {
        Room room = org.mockito.Mockito.mock(Room.class);
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(true);

        useCase.execute("u1", "r1", new UpdateRoomTitleRequest("  new title  "));

        verify(room).updateTitle("new title");
    }

    @Test
    @DisplayName("Should throw when user is not host")
    void execute_WhenNotHost_Throws() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("u1", "r1", new UpdateRoomTitleRequest("title")))
                .isInstanceOf(RestApiException.class);
    }
}
