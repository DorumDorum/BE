package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadRoomApplicationsUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadRoomApplicationsUseCase Unit Tests")
class LoadRoomApplicationsUseCaseTest {

    @Mock private RoomRequestService roomRequestService;
    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @InjectMocks private LoadRoomApplicationsUseCase useCase;

    @Test
    @DisplayName("Should return room application list for roommate user")
    void execute_WhenAuthorized_ReturnsApplications() {
        Room room = Room.builder().roomNo("r1").build();
        List<RoomRequestApplicationResponse> expected = List.of(
                RoomRequestApplicationResponse.builder().requestNo("rq1").userNo("u2").name("name").build()
        );
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.isUserRoommate("u1", "r1")).thenReturn(true);
        when(roomRequestService.findApplicationsByRoom(room)).thenReturn(expected);

        List<RoomRequestApplicationResponse> result = useCase.execute("u1", "r1");

        assertThat(result).isEqualTo(expected);
        verify(roomRequestService).findApplicationsByRoom(room);
    }

    @Test
    @DisplayName("Should throw when user has no permission on room")
    void execute_WhenUnauthorized_Throws() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.isUserRoommate("u1", "r1")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("u1", "r1"))
                .isInstanceOf(RestApiException.class);
    }
}
