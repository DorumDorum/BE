package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.FindRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.FindRoomsController;
import com.project.dorumdorum.global.common.BaseResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindRoomsController Unit Tests")
class FindRoomsControllerTest {

    @Mock private FindRoomsUseCase useCase;
    @InjectMocks private FindRoomsController controller;

    @Test
    void loadAll_ReturnsUseCaseResult() {
        CursorPage<FindRoomsResponse> page = new CursorPage<>(List.of(), null, false);
        when(useCase.execute(RoomRelation.RECRUITING, null, null, null, RoomSort.CREATED_AT, null)).thenReturn(page);

        BaseResponse<CursorPage<FindRoomsResponse>> response =
                controller.loadAll(RoomRelation.RECRUITING, null, null, null, RoomSort.CREATED_AT, null);

        verify(useCase).execute(RoomRelation.RECRUITING, null, null, null, RoomSort.CREATED_AT, null);
        assertThat(response.getResult()).isEqualTo(page);
    }
}
