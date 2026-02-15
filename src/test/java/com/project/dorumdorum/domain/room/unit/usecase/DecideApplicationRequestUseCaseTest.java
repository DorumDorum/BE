package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.usecase.DecideApplicationRequestUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DecideApplicationRequestUseCase Unit Tests")
class DecideApplicationRequestUseCaseTest {

    @Mock private RoomRequestService roomRequestService;
    @Mock private UserService userService;
    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @InjectMocks private DecideApplicationRequestUseCase useCase;

    @Test
    @DisplayName("Should approve request when host and applicant not joined")
    void approve_WhenValid_ApprovesAndDeletesRequest() {
        Room room = mock(Room.class);
        RoomRequest request = RoomRequest.builder().roomRequestNo("rq1").userNo("applicant").build();
        when(roomRequestService.findById("rq1")).thenReturn(request);
        when(roommateService.existsByUserNo("applicant")).thenReturn(false);
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.isHost("host", room)).thenReturn(true);

        useCase.approve("host", "r1", "rq1");

        verify(userService).validateExistsById("host");
        verify(room).plusCurrentMate();
        verify(roommateService).create("applicant", room, RoomRole.MEMBER);
        verify(roomRequestService).delete(request);
    }

    @Test
    @DisplayName("Should throw when applicant already joined another room")
    void approve_WhenApplicantAlreadyJoined_Throws() {
        RoomRequest request = RoomRequest.builder().roomRequestNo("rq1").userNo("applicant").build();
        when(roomRequestService.findById("rq1")).thenReturn(request);
        when(roommateService.existsByUserNo("applicant")).thenReturn(true);

        assertThatThrownBy(() -> useCase.approve("host", "r1", "rq1"))
                .isInstanceOf(RestApiException.class);

        verify(roomService, never()).findById(anyString());
        verify(roomRequestService, never()).delete(any(RoomRequest.class));
    }

    @Test
    @DisplayName("Should throw when approver is not host")
    void approve_WhenNotHost_Throws() {
        Room room = Room.builder().roomNo("r1").build();
        RoomRequest request = RoomRequest.builder().roomRequestNo("rq1").userNo("applicant").build();
        when(roomRequestService.findById("rq1")).thenReturn(request);
        when(roommateService.existsByUserNo("applicant")).thenReturn(false);
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.isHost("host", room)).thenReturn(false);

        assertThatThrownBy(() -> useCase.approve("host", "r1", "rq1"))
                .isInstanceOf(RestApiException.class);

        verify(roomRequestService, never()).delete(any(RoomRequest.class));
    }

    @Test
    @DisplayName("Should reject request when user is host")
    void reject_WhenHost_DeletesRequest() {
        Room room = Room.builder().roomNo("r1").build();
        RoomRequest request = RoomRequest.builder().roomRequestNo("rq1").room(room).userNo("applicant").build();
        when(roomRequestService.findById("rq1")).thenReturn(request);
        when(roommateService.isHost("host", room)).thenReturn(true);

        useCase.reject("host", "rq1");

        verify(userService).validateExistsById("host");
        verify(roomRequestService).delete(request);
    }

    @Test
    @DisplayName("Should throw on reject when user is not host")
    void reject_WhenNotHost_Throws() {
        Room room = Room.builder().roomNo("r1").build();
        RoomRequest request = RoomRequest.builder().roomRequestNo("rq1").room(room).userNo("applicant").build();
        when(roomRequestService.findById("rq1")).thenReturn(request);
        when(roommateService.isHost("host", room)).thenReturn(false);

        assertThatThrownBy(() -> useCase.reject("host", "rq1"))
                .isInstanceOf(RestApiException.class);

        verify(roomRequestService, never()).delete(any(RoomRequest.class));
    }
}
