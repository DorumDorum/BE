package com.project.dorumdorum.domain.checklist.unit.ui;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.usecase.UpdateRoomRuleUseCase;
import com.project.dorumdorum.domain.checklist.ui.UpdateRoomRuleController;
import com.project.dorumdorum.domain.checklist.unit.ChecklistFixtures;
import com.project.dorumdorum.global.common.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateRoomRuleController Unit Tests")
class UpdateRoomRuleControllerTest {

    @Mock private UpdateRoomRuleUseCase useCase;
    @InjectMocks private UpdateRoomRuleController controller;

    @Test
    void update_CallsUseCase() {
        UpdateRoomRuleRequest request = ChecklistFixtures.updateRoomRuleRequest();
        BaseResponse<Void> response = controller.update("u1", "r1", request);

        verify(useCase).execute("u1", "r1", request);
        assertThat(response.getCode()).isEqualTo("COMMON200");
    }
}
