package com.project.dorumdorum.domain.roommate.unit.entity;

import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Roommate Entity Tests")
class RoommateTest {

    @Test
    @DisplayName("Should create roommate with builder")
    void builder_WithRequiredFields_CreatesRoommate() {
        Roommate roommate = Roommate.builder()
                .roommateNo("rm1")
                .userNo("u1")
                .confirmStatus(ConfirmStatus.PENDING)
                .roomRole(RoomRole.MEMBER)
                .build();

        assertThat(roommate).isNotNull();
        assertThat(roommate.getRoommateNo()).isEqualTo("rm1");
        assertThat(roommate.getUserNo()).isEqualTo("u1");
        assertThat(roommate.getConfirmStatus()).isEqualTo(ConfirmStatus.PENDING);
        assertThat(roommate.getRoomRole()).isEqualTo(RoomRole.MEMBER);
    }

    @Test
    @DisplayName("Should initialize confirm status to pending on pre-persist")
    void init_SetsConfirmStatusToPending() {
        Roommate roommate = Roommate.builder()
                .roommateNo("rm1")
                .userNo("u1")
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.ACCEPTED)
                .build();

        roommate.init();

        assertThat(roommate.getConfirmStatus()).isEqualTo(ConfirmStatus.PENDING);
    }

    @Test
    @DisplayName("Should return true when status is completed")
    void isCompleted_WhenCompleted_ReturnsTrue() {
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.COMPLETED)
                .build();

        assertThat(roommate.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("Should return false when status is not completed")
    void isCompleted_WhenNotCompleted_ReturnsFalse() {
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.PENDING)
                .build();

        assertThat(roommate.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("Should change status to accepted")
    void accept_ChangesStatusToAccepted() {
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.PENDING)
                .build();

        roommate.accept();

        assertThat(roommate.getConfirmStatus()).isEqualTo(ConfirmStatus.ACCEPTED);
    }

    @Test
    @DisplayName("Should change status to completed")
    void complete_ChangesStatusToCompleted() {
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.ACCEPTED)
                .build();

        roommate.complete();

        assertThat(roommate.getConfirmStatus()).isEqualTo(ConfirmStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should change status back to pending")
    void cancelConfirm_ChangesStatusToPending() {
        Roommate roommate = Roommate.builder()
                .userNo("u1")
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.ACCEPTED)
                .build();

        roommate.cancelConfirm();

        assertThat(roommate.getConfirmStatus()).isEqualTo(ConfirmStatus.PENDING);
    }
}
