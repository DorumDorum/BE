package com.project.dorumdorum.domain.room.unit.service;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomService Unit Tests")
class RoomServiceTest {

    @Mock private RoomRepository roomRepository;
    @InjectMocks private RoomService service;

    @Test
    @DisplayName("Should create and save room from request")
    void create_SavesRoom() {
        RoomCreateRequest request = new RoomCreateRequest(RoomType.TYPE_1, 2, ResidencePeriod.SEMESTER, "title", null);
        Room saved = Room.builder().roomNo("r1").hostUserNo("u1").build();
        when(roomRepository.save(any(Room.class))).thenReturn(saved);

        Room result = service.create("u1", request);

        assertThat(result).isEqualTo(saved);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    @DisplayName("Should throw when room id not found")
    void findById_WhenMissing_Throws() {
        when(roomRepository.findById("r1")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById("r1")).isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should delegate cursor search to repository")
    void searchByCursor_DelegatesToRepository() {
        List<FindRoomsResponse> expected = List.of(
                new FindRoomsResponse("r1", RoomType.TYPE_1, 2, 1, LocalDateTime.now(),
                        "title", "host", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name(), 1)
        );
        ChecklistFilterRequest request = new ChecklistFilterRequest(
                ChecklistFilterRequest.SortType.LATEST, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null
        );
        when(roomRepository.findByCursor(any(), any(), any(), any(), anyInt()))
                .thenReturn(expected);

        List<FindRoomsResponse> result = service.searchByCursor(
                request, null, null, null, 10
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should throw when my room is not found")
    void findMyRoom_WhenMissing_Throws() {
        when(roomRepository.findMyRoom("u1")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findMyRoom("u1")).isInstanceOf(RestApiException.class);
    }
}
