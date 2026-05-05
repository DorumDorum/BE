package com.project.dorumdorum.domain.roommate.unit.service;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateRepository;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoommateService Unit Tests")
class RoommateServiceTest {

    @Mock
    private RoommateRepository roommateRepository;

    @InjectMocks
    private RoommateService roommateService;

    @Test
    @DisplayName("Should create roommate with pending status")
    void create_SavesPendingRoommate() {
        Room room = Room.builder().roomNo("room1").build();
        Roommate saved = Roommate.builder()
                .roommateNo("rm1")
                .userNo("u1")
                .room(room)
                .confirmStatus(ConfirmStatus.PENDING)
                .roomRole(RoomRole.MEMBER)
                .build();
        when(roommateRepository.save(any(Roommate.class))).thenReturn(saved);

        Roommate result = roommateService.create("u1", room, RoomRole.MEMBER);

        assertThat(result).isEqualTo(saved);
        verify(roommateRepository).save(any(Roommate.class));
    }

    @Test
    @DisplayName("Should return exists by user and room")
    void isUserRoommate_ReturnsRepositoryResult() {
        when(roommateRepository.existsByUserNoAndRoomNo("u1", "room1")).thenReturn(true);

        Boolean result = roommateService.isUserRoommate("u1", "room1");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return exists by user")
    void existsByUserNo_ReturnsRepositoryResult() {
        when(roommateRepository.existsByUserNo("u1")).thenReturn(false);

        Boolean result = roommateService.existsByUserNo("u1");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when role is host")
    void isHost_WhenHostRole_ReturnsTrue() {
        Room room = Room.builder().roomNo("room1").build();
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .room(room)
                .roomRole(RoomRole.HOST)
                .confirmStatus(ConfirmStatus.PENDING)
                .build();
        when(roommateRepository.findByUserNoAndRoom("u1", room)).thenReturn(Optional.of(roommate));

        Boolean result = roommateService.isHost("u1", room);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when role is member")
    void isHost_WhenMemberRole_ReturnsFalse() {
        Room room = Room.builder().roomNo("room1").build();
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .room(room)
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.PENDING)
                .build();
        when(roommateRepository.findByUserNoAndRoom("u1", room)).thenReturn(Optional.of(roommate));

        Boolean result = roommateService.isHost("u1", room);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should throw when roommate not found by user and room")
    void findByUserNoAndRoom_WhenNotFound_Throws() {
        Room room = Room.builder().roomNo("room1").build();
        when(roommateRepository.findByUserNoAndRoom("u1", room)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roommateService.findByUserNoAndRoom("u1", room))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should return roommates by room")
    void findByRoom_ReturnsRepositoryResult() {
        Room room = Room.builder().roomNo("room1").build();
        List<Roommate> expected = List.of(Roommate.builder()
                .roommateNo("rm1").userNo("u1").room(room)
                .confirmStatus(ConfirmStatus.PENDING).roomRole(RoomRole.MEMBER).build());
        when(roommateRepository.findByRoom(room)).thenReturn(expected);

        List<Roommate> result = roommateService.findByRoom(room);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return optional roommate by user number")
    void findByUserNo_ReturnsRepositoryResult() {
        Optional<Roommate> expected = Optional.of(Roommate.builder()
                .roommateNo("rm1").userNo("u1")
                .confirmStatus(ConfirmStatus.PENDING).roomRole(RoomRole.MEMBER).build());
        when(roommateRepository.findByUserNo("u1")).thenReturn(expected);

        Optional<Roommate> result = roommateService.findByUserNo("u1");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return my roommates response list")
    void findMyRoommates_ReturnsRepositoryResult() {
        List<MyRoommateResponse> expected = List.of(
                new MyRoommateResponse("rm1", "u1", ConfirmStatus.PENDING, RoomRole.MEMBER,
                        "name", "nick", "20210001", "major", "3", 25, Gender.MALE, true)
        );
        when(roommateRepository.findMyRoommates("u1")).thenReturn(expected);

        List<MyRoommateResponse> result = roommateService.findMyRoommates("u1");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("isHostOfRoom: 룸메이트가 HOST 역할이면 true를 반환한다")
    void isHostOfRoom_WhenHostRole_ReturnsTrue() {
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .roomRole(RoomRole.HOST)
                .confirmStatus(ConfirmStatus.PENDING)
                .build();
        when(roommateRepository.findByUserNoAndRoomNo("u1", "room1")).thenReturn(Optional.of(roommate));

        boolean result = roommateService.isHostOfRoom("u1", "room1");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isHostOfRoom: 룸메이트가 MEMBER 역할이면 false를 반환한다")
    void isHostOfRoom_WhenMemberRole_ReturnsFalse() {
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.PENDING)
                .build();
        when(roommateRepository.findByUserNoAndRoomNo("u1", "room1")).thenReturn(Optional.of(roommate));

        boolean result = roommateService.isHostOfRoom("u1", "room1");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isHostOfRoom: 룸메이트를 찾지 못하면 false를 반환한다")
    void isHostOfRoom_WhenNotFound_ReturnsFalse() {
        when(roommateRepository.findByUserNoAndRoomNo("u1", "room1")).thenReturn(Optional.empty());

        boolean result = roommateService.isHostOfRoom("u1", "room1");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("leaveRoom: 룸메이트를 소프트 삭제하고 저장한다")
    void leaveRoom_SoftDeletesRoommate() {
        Roommate roommate = Roommate.builder()
                .roommateNo("rm1")
                .userNo("u1")
                .confirmStatus(ConfirmStatus.ACCEPTED)
                .roomRole(RoomRole.MEMBER)
                .build();
        when(roommateRepository.findByUserNoAndRoomNo("u1", "room1")).thenReturn(Optional.of(roommate));

        roommateService.leaveRoom("u1", "room1");

        ArgumentCaptor<Roommate> captor = ArgumentCaptor.forClass(Roommate.class);
        verify(roommateRepository).save(captor.capture());
        assertThat(captor.getValue().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("leaveRoom: 룸메이트를 찾지 못하면 예외를 던진다")
    void leaveRoom_WhenNotFound_Throws() {
        when(roommateRepository.findByUserNoAndRoomNo("u1", "room1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roommateService.leaveRoom("u1", "room1"))
                .isInstanceOf(RestApiException.class);
    }
}
