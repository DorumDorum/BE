package com.project.dorumdorum.domain.checklist.unit.ui;

import com.project.dorumdorum.domain.checklist.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.checklist.application.usecase.LoadMyRoomRuleUseCase;
import com.project.dorumdorum.domain.checklist.ui.LoadMyRoomRuleController;
import com.project.dorumdorum.global.common.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadMyRoomRuleController Unit Tests")
class LoadMyRoomRuleControllerTest {

    @Mock private LoadMyRoomRuleUseCase useCase;
    @InjectMocks private LoadMyRoomRuleController controller;

    @Test
    void load_ReturnsUseCaseResult() {
        MyRoomRuleResponse payload = MyRoomRuleResponse.builder().bedtime("23:00").build();
        when(useCase.execute("r1")).thenReturn(payload);

        BaseResponse<MyRoomRuleResponse> response = controller.load("r1");

        verify(useCase).execute("r1");
        assertThat(response.getResult()).isEqualTo(payload);
    }
}
