package com.project.dorumdorum.domain.room.unit.service;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestRepository;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomRequestService Unit Tests")
class RoomRequestServiceTest {

    @Mock private RoomRequestRepository roomRequestRepository;
    @InjectMocks private RoomRequestService service;

    @Test
    @DisplayName("Should create room request and save")
    void create_SavesEntity() {
        Room room = Room.builder().roomNo("r1").build();
        JoinRoomRequest req = new JoinRoomRequest("intro", "msg");
        RoomRequest saved = RoomRequest.builder().roomRequestNo("rq1").room(room).userNo("u1")
                .direction(Direction.USER_TO_ROOM).introduction("intro").additionalMessage("msg").build();
        when(roomRequestRepository.save(any(RoomRequest.class))).thenReturn(saved);

        RoomRequest result = service.create("u1", room, req, Direction.USER_TO_ROOM);

        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("Should return duplicate request existence")
    void isDuplicateJoinRequest_ReturnsRepositoryResult() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomRequestRepository.existsByUserNoAndRoom("u1", room)).thenReturn(true);
        assertThat(service.isDuplicateJoinRequest("u1", room)).isTrue();
    }

    @Test
    @DisplayName("Should throw when request not found by id")
    void findById_WhenMissing_Throws() {
        when(roomRequestRepository.findById("rq1")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById("rq1")).isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should soft delete provided request entity")
    void delete_SoftDeletesEntity() {
        RoomRequest request = RoomRequest.builder().roomRequestNo("rq1").build();
        service.delete(request);
        assertThat(request.getDeletedAt()).isNotNull();
        verify(roomRequestRepository).save(request);
    }

    @Test
    @DisplayName("Should return applications by room")
    void findApplicationsByRoom_ReturnsRepositoryResult() {
        Room room = Room.builder().roomNo("r1").build();
        List<RoomRequestApplicationResponse> expected = List.of(
                RoomRequestApplicationResponse.builder()
                        .requestNo("rq1").userNo("u1").name("name").nickname("nick")
                        .major("major").studentNo("20210001").grade("3").age(25)
                        .createdAt(LocalDateTime.now()).introduction("intro").additionalMessage("msg")
                        .build()
        );
        when(roomRequestRepository.findApplicationsByRoom(room)).thenReturn(expected);

        List<RoomRequestApplicationResponse> result = service.findApplicationsByRoom(room);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should cancel join request by soft deleting request")
    void cancelJoinRequest_FindsAndSoftDeletesRequest() {
        Room room = Room.builder().roomNo("r1").build();
        RoomRequest request = RoomRequest.builder().roomRequestNo("rq1").build();
        when(roomRequestRepository.findByUserNoAndRoomAndDirection("u1", room, Direction.USER_TO_ROOM))
                .thenReturn(Optional.of(request));

        service.cancelJoinRequest("u1", room);

        assertThat(request.getDeletedAt()).isNotNull();
        verify(roomRequestRepository).save(request);
    }

    @Test
    @DisplayName("Should throw when cancel target request not found")
    void cancelJoinRequest_WhenMissing_Throws() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomRequestRepository.findByUserNoAndRoomAndDirection("u1", room, Direction.USER_TO_ROOM))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancelJoinRequest("u1", room))
                .isInstanceOf(RestApiException.class);
    }
}
