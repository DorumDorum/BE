package com.project.dorumdorum.domain.roommate.unit.ui;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.application.usecase.LoadMyRoommatesUseCase;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.ui.LoadMyRoommatesController;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.global.common.BaseResponse;
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
@DisplayName("LoadMyRoommatesController Unit Tests")
class LoadMyRoommatesControllerTest {

    @Mock
    private LoadMyRoommatesUseCase loadMyRoommatesUseCase;

    @InjectMocks
    private LoadMyRoommatesController controller;

    @Test
    @DisplayName("Should return roommate list from use case")
    void load_ReturnsRoommatesFromUseCase() {
        String userNo = "u1";
        List<MyRoommateResponse> expected = List.of(
                new MyRoommateResponse("rm1", "u1", ConfirmStatus.PENDING, RoomRole.HOST,
                        "name", "nick", "20210001", "major", "3", 25, Gender.MALE, true)
        );
        when(loadMyRoommatesUseCase.execute(userNo)).thenReturn(expected);

        BaseResponse<List<MyRoommateResponse>> response = controller.load(userNo);

        verify(loadMyRoommatesUseCase).execute(userNo);
        assertThat(response.getResult()).isEqualTo(expected);
    }
}
