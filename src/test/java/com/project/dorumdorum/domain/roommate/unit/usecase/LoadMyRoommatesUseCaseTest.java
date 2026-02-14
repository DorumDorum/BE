package com.project.dorumdorum.domain.roommate.unit.usecase;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.application.usecase.LoadMyRoommatesUseCase;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadMyRoommatesUseCase Unit Tests")
class LoadMyRoommatesUseCaseTest {

    @Mock
    private RoommateService roommateService;

    @InjectMocks
    private LoadMyRoommatesUseCase loadMyRoommatesUseCase;

    @Test
    @DisplayName("Should return roommates from service")
    void execute_ReturnsRoommatesFromService() {
        String userNo = "0000000000000001";
        List<MyRoommateResponse> expected = List.of(
                new MyRoommateResponse("rm1", userNo, ConfirmStatus.PENDING, RoomRole.HOST,
                        "name", "nick", "20210001", "major", "3", 25, Gender.MALE, true)
        );
        when(roommateService.findMyRoommates(userNo)).thenReturn(expected);

        List<MyRoommateResponse> result = loadMyRoommatesUseCase.execute(userNo);

        assertThat(result).isEqualTo(expected);
        verify(roommateService).findMyRoommates(userNo);
    }
}
