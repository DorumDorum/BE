package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.FindRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.FindRoomsController;
import com.project.dorumdorum.global.pagination.CursorPage;
import org.springframework.http.ResponseEntity;
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
@DisplayName("FindRoomsController Unit Tests")
class FindRoomsControllerTest {

    @Mock private FindRoomsUseCase useCase;
    @InjectMocks private FindRoomsController controller;

    private static final String USER_NO = "u1";

    @Test
    void loadAll_ReturnsUseCaseResult() {
        ChecklistFilterRequest request = new ChecklistFilterRequest(
                ChecklistFilterRequest.SortType.LATEST, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null
        );
        CursorPage<FindRoomsResponse> page = new CursorPage<>(List.of(), null, false);
        when(useCase.execute(USER_NO, request)).thenReturn(page);

        ResponseEntity<CursorPage<FindRoomsResponse>> response =
                controller.loadAll(USER_NO, request);

        verify(useCase).execute(USER_NO, request);
        assertThat(response.getBody()).isEqualTo(page);
    }
}
