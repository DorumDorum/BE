package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.usecase.ConfirmRoomAssignmentUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfirmRoomAssignmentUseCase Unit Tests")
class ConfirmRoomAssignmentUseCaseTest {

    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @InjectMocks private ConfirmRoomAssignmentUseCase useCase;

    @Test
    @DisplayName("Should accept current roommate when user is in room")
    void execute_WhenUserInRoom_AcceptsCurrentRoommate() {
        Room room = mock(Room.class);
        Roommate me = mock(Roommate.class);
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.findByRoom(room)).thenReturn(List.of(me));
        when(me.getUserNo()).thenReturn("u1");
        when(room.isFull()).thenReturn(false);

        useCase.execute("u1", "r1");

        verify(me).accept();
        verify(room, never()).updateStatus(RoomStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should complete all and update room status when full and all accepted")
    void execute_WhenFullAndAllAccepted_CompletesRoom() {
        Room room = mock(Room.class);
        Roommate me = mock(Roommate.class);
        Roommate other = mock(Roommate.class);
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.findByRoom(room)).thenReturn(List.of(me, other));
        when(me.getUserNo()).thenReturn("u1");
        when(room.isFull()).thenReturn(true);
        when(me.getConfirmStatus()).thenReturn(ConfirmStatus.ACCEPTED);
        when(other.getConfirmStatus()).thenReturn(ConfirmStatus.ACCEPTED);

        useCase.execute("u1", "r1");

        verify(me).complete();
        verify(other).complete();
        verify(room).updateStatus(RoomStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should throw when user is not a roommate in the room")
    void execute_WhenUserNotInRoom_Throws() {
        Room room = Room.builder().roomNo("r1").build();
        Roommate other = Roommate.builder().userNo("u2").confirmStatus(ConfirmStatus.PENDING).roomRole(RoomRole.MEMBER).build();
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.findByRoom(room)).thenReturn(List.of(other));

        assertThatThrownBy(() -> useCase.execute("u1", "r1"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should not complete room when full but not all accepted")
    void execute_WhenFullButNotAllAccepted_DoesNotCompleteRoom() {
        Room room = mock(Room.class);
        Roommate me = mock(Roommate.class);
        Roommate other = mock(Roommate.class);
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.findByRoom(room)).thenReturn(List.of(me, other));
        when(me.getUserNo()).thenReturn("u1");
        when(room.isFull()).thenReturn(true);
        when(me.getConfirmStatus()).thenReturn(ConfirmStatus.ACCEPTED);
        when(other.getConfirmStatus()).thenReturn(ConfirmStatus.PENDING);

        useCase.execute("u1", "r1");

        verify(me).accept();
        verify(me, never()).complete();
        verify(other, never()).complete();
        verify(room, never()).updateStatus(RoomStatus.COMPLETED);
    }
}
