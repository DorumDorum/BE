package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.usecase.CancelRoomApplicationUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CancelRoomApplicationUseCase Unit Tests")
class CancelRoomApplicationUseCaseTest {

    @Mock private RoomService roomService;
    @Mock private RoomRequestService roomRequestService;
    @InjectMocks private CancelRoomApplicationUseCase useCase;

    @Test
    @DisplayName("Should cancel user join request for room")
    void execute_CancelsJoinRequest() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomService.findById("r1")).thenReturn(room);

        useCase.execute("u1", "r1");

        verify(roomService).findById("r1");
        verify(roomRequestService).cancelJoinRequest("u1", room);
    }
}
