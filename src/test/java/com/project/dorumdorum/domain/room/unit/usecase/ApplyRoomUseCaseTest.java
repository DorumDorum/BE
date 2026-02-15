package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.usecase.ApplyRoomUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplyRoomUseCase Unit Tests")
class ApplyRoomUseCaseTest {

    @Mock private UserService userService;
    @Mock private RoomRequestService roomRequestService;
    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;

    @InjectMocks private ApplyRoomUseCase useCase;

    @Test
    @DisplayName("Should create join request when validations pass")
    void execute_WithValidState_CreatesJoinRequest() {
        String userNo = "u1";
        String roomNo = "r1";
        Room room = Room.builder().roomNo(roomNo).build();
        JoinRoomRequest request = new JoinRoomRequest("intro", "msg");
        RoomRequest createdRequest = RoomRequest.builder()
                .roomRequestNo("req-1")
                .room(room)
                .userNo(userNo)
                .direction(Direction.USER_TO_ROOM)
                .introduction(request.introduction())
                .additionalMessage(request.additionalMessage())
                .build();

        when(roomService.findById(roomNo)).thenReturn(room);
        when(roommateService.isUserRoommate(userNo, roomNo)).thenReturn(false);
        when(roommateService.existsByUserNo(userNo)).thenReturn(false);
        when(roomRequestService.isDuplicateJoinRequest(userNo, room)).thenReturn(false);
        when(roomRequestService.create(userNo, room, request, Direction.USER_TO_ROOM)).thenReturn(createdRequest);

        String requestNo = useCase.execute(userNo, roomNo, request);

        verify(userService).validateExistsById(userNo);
        verify(roomRequestService).create(userNo, room, request, Direction.USER_TO_ROOM);
        assertThat(requestNo).isEqualTo("req-1");
    }

    @Test
    @DisplayName("Should throw when user already belongs to the room")
    void execute_WhenAlreadyRoommate_Throws() {
        String userNo = "u1";
        String roomNo = "r1";
        when(roomService.findById(roomNo)).thenReturn(Room.builder().roomNo(roomNo).build());
        when(roommateService.isUserRoommate(userNo, roomNo)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(userNo, roomNo, new JoinRoomRequest("intro", null)))
                .isInstanceOf(RestApiException.class);

        verify(roomRequestService, never()).create(anyString(), any(), any(), eq(Direction.USER_TO_ROOM));
    }

    @Test
    @DisplayName("Should throw when user already joined another room")
    void execute_WhenAlreadyJoined_Throws() {
        String userNo = "u1";
        String roomNo = "r1";
        Room room = Room.builder().roomNo(roomNo).build();
        when(roomService.findById(roomNo)).thenReturn(room);
        when(roommateService.isUserRoommate(userNo, roomNo)).thenReturn(false);
        when(roommateService.existsByUserNo(userNo)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(userNo, roomNo, new JoinRoomRequest("intro", null)))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should throw when join request is duplicate")
    void execute_WhenDuplicateRequest_Throws() {
        String userNo = "u1";
        String roomNo = "r1";
        Room room = Room.builder().roomNo(roomNo).build();
        when(roomService.findById(roomNo)).thenReturn(room);
        when(roommateService.isUserRoommate(userNo, roomNo)).thenReturn(false);
        when(roommateService.existsByUserNo(userNo)).thenReturn(false);
        when(roomRequestService.isDuplicateJoinRequest(userNo, room)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(userNo, roomNo, new JoinRoomRequest("intro", null)))
                .isInstanceOf(RestApiException.class);
    }
}
