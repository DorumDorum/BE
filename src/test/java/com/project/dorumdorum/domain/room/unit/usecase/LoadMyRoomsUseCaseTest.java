package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadMyRoomsUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadMyRoomsUseCase Unit Tests")
class LoadMyRoomsUseCaseTest {

    @Mock private RoomService roomService;
    @InjectMocks private LoadMyRoomsUseCase useCase;

    @Test
    @DisplayName("Should return my room from room service")
    void execute_ReturnsMyRoom() {
        FindRoomsResponse expected = new FindRoomsResponse("r1", RoomType.TYPE_1, 2, 1, LocalDateTime.now(),
                "title", "host", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name());
        when(roomService.findMyRoom("u1")).thenReturn(expected);

        FindRoomsResponse result = useCase.execute("u1");

        assertThat(result).isEqualTo(expected);
        verify(roomService).findMyRoom("u1");
    }
}
