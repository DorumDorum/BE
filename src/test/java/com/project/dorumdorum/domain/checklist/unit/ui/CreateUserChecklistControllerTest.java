package com.project.dorumdorum.domain.checklist.unit.ui;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.usecase.CreateUserChecklistUseCase;
import com.project.dorumdorum.domain.checklist.ui.CreateUserChecklistController;
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
@DisplayName("CreateUserChecklistController Unit Tests")
class CreateUserChecklistControllerTest {

    @Mock private CreateUserChecklistUseCase useCase;
    @InjectMocks private CreateUserChecklistController controller;

    @Test
    void create_CallsUseCase() {
        CreateUserChecklistRequest request = ChecklistFixtures.createUserChecklistRequest();
        BaseResponse<Void> response = controller.create("u1", request);

        verify(useCase).execute("u1", request);
        assertThat(response.getCode()).isEqualTo("COMMON200");
    }
}
