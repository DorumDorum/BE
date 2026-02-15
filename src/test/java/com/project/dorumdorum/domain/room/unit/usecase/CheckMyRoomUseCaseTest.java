package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.CheckMyRoomResponse;
import com.project.dorumdorum.domain.room.application.usecase.CheckMyRoomUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheckMyRoomUseCase Unit Tests")
class CheckMyRoomUseCaseTest {

    @Mock private RoommateService roommateService;
    @InjectMocks private CheckMyRoomUseCase useCase;

    @Test
    @DisplayName("Should return existing room info when roommate exists")
    void execute_WhenRoommateExists_ReturnsExistResponse() {
        Room room = Room.builder().roomNo("r1").build();
        Roommate roommate = Roommate.builder().room(room).userNo("u1")
                .confirmStatus(ConfirmStatus.PENDING).roomRole(RoomRole.MEMBER).build();
        when(roommateService.findByUserNo("u1")).thenReturn(Optional.of(roommate));

        CheckMyRoomResponse result = useCase.execute("u1");

        assertThat(result.isExist()).isTrue();
        assertThat(result.roomNo()).isEqualTo("r1");
    }

    @Test
    @DisplayName("Should return not-exist response when roommate does not exist")
    void execute_WhenRoommateMissing_ReturnsNotExistResponse() {
        when(roommateService.findByUserNo("u1")).thenReturn(Optional.empty());

        CheckMyRoomResponse result = useCase.execute("u1");

        assertThat(result.isExist()).isFalse();
        assertThat(result.roomNo()).isNull();
    }
}
