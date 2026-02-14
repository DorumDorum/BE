package com.project.dorumdorum.domain.checklist.unit.ui;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.usecase.UpdateUserChecklistUseCase;
import com.project.dorumdorum.domain.checklist.ui.UpdateUserChecklistController;
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
@DisplayName("UpdateUserChecklistController Unit Tests")
class UpdateUserChecklistControllerTest {

    @Mock private UpdateUserChecklistUseCase useCase;
    @InjectMocks private UpdateUserChecklistController controller;

    @Test
    void update_CallsUseCase() {
        UpdateUserChecklistRequest request = ChecklistFixtures.updateUserChecklistRequest();
        BaseResponse<Void> response = controller.update("u1", request);

        verify(useCase).execute("u1", request);
        assertThat(response.getCode()).isEqualTo("COMMON200");
    }
}
