package com.project.dorumdorum.domain.room.unit.entity;

import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Room Entity Unit Tests")
class RoomTest {

    private Room newRoom() {
        return Room.builder()
                .roomNo("r1")
                .roomType(RoomType.TYPE_1)
                .capacity(2)
                .title("old title")
                .hostUserNo("host")
                .residencePeriod(ResidencePeriod.SEMESTER)
                .build();
    }

    @Test
    @DisplayName("Should initialize defaults on pre-persist init")
    void init_SetsDefaultFields() {
        Room room = newRoom();

        room.init();

        assertThat(room.getCurrentMateCount()).isEqualTo(1);
        assertThat(room.getConfirmMateCount()).isEqualTo(0);
        assertThat(room.getRemaining()).isEqualTo(1);
        assertThat(room.getRoomStatus()).isEqualTo(RoomStatus.CONFIRM_PENDING);
        assertThat(room.isPending()).isTrue();
        assertThat(room.isHost("host")).isTrue();
        assertThat(room.isHost("other")).isFalse();
    }

    @Test
    @DisplayName("Should update counters and fields correctly")
    void mutationMethods_UpdateState() {
        Room room = newRoom();
        room.init();

        room.plusCurrentMate();
        assertThat(room.getCurrentMateCount()).isEqualTo(2);
        assertThat(room.getRemaining()).isEqualTo(0);
        assertThat(room.isFull()).isTrue();

        room.plusConfirmMate();
        assertThat(room.getConfirmMateCount()).isEqualTo(1);

        room.clearConfirmMate();
        assertThat(room.getConfirmMateCount()).isZero();

        room.minusCurrentMate();
        assertThat(room.getCurrentMateCount()).isEqualTo(1);
        assertThat(room.getRemaining()).isEqualTo(1);
        assertThat(room.isFull()).isFalse();

        room.updateStatus(RoomStatus.COMPLETED);
        assertThat(room.getRoomStatus()).isEqualTo(RoomStatus.COMPLETED);
        assertThat(room.isPending()).isFalse();

        room.updateCapacity(3);
        assertThat(room.getCapacity()).isEqualTo(3);
        assertThat(room.getRemaining()).isEqualTo(2);

        room.updateRoomType(RoomType.TYPE_2);
        room.updateResidencePeriod(ResidencePeriod.HALF_YEAR);
        room.updateTitle("new title");

        assertThat(room.getRoomType()).isEqualTo(RoomType.TYPE_2);
        assertThat(room.getResidencePeriod()).isEqualTo(ResidencePeriod.HALF_YEAR);
        assertThat(room.getTitle()).isEqualTo("new title");
    }

    @Test
    @DisplayName("Should validate capacity is not lower than current mate count")
    void isValidCapacity_WhenLowerThanCurrentMateCount_ReturnsFalse() {
        Room room = newRoom();
        room.init();
        room.plusCurrentMate();

        assertThat(room.isValidCapacity(1)).isFalse();
        assertThat(room.isValidCapacity(2)).isTrue();
    }
}
