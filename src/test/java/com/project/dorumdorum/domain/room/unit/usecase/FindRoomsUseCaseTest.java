package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.FindRoomsUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.global.pagination.CursorPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindRoomsUseCase Unit Tests")
class FindRoomsUseCaseTest {

    @Mock private RoomService roomService;
    @InjectMocks private FindRoomsUseCase useCase;

    private FindRoomsResponse room(String roomNo, int capacity, int current, LocalDateTime createdAt) {
        return new FindRoomsResponse(roomNo, RoomType.TYPE_1, capacity, current, createdAt, "title",
                "host", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name());
    }

    @Test
    @DisplayName("Should return hasNext true when responses exceed limit")
    void execute_WhenResponsesExceedLimit_ReturnsHasNextTrue() {
        LocalDateTime now = LocalDateTime.now();
        List<FindRoomsResponse> responses = java.util.stream.IntStream.rangeClosed(1, 51)
                .mapToObj(i -> room("r" + i, 2, 1, now.minusMinutes(i)))
                .toList();
        when(roomService.searchByCursor(any(), any(), any(), any(), any(), any(), any(Integer.class))).thenReturn(responses);

        CursorPage<FindRoomsResponse> result = useCase.execute(RoomRelation.RECRUITING, null, null, null, RoomSort.CREATED_AT, null);

        assertThat(result.items()).hasSize(50);
        assertThat(result.hasNext()).isFalse();
        assertThat(result.nextCursor()).isNotBlank();
        verify(roomService).searchByCursor(any(), any(), any(), any(), any(), any(), any(Integer.class));
    }

    @Test
    @DisplayName("Should return hasNext false when responses within limit")
    void execute_WhenResponsesWithinLimit_ReturnsHasNextFalse() {
        List<FindRoomsResponse> responses = List.of(room("r1", 2, 1, LocalDateTime.now()));
        when(roomService.searchByCursor(any(), any(), any(), any(), any(), any(), any(Integer.class))).thenReturn(responses);

        CursorPage<FindRoomsResponse> result = useCase.execute(RoomRelation.RECRUITING, null, null, null, RoomSort.REMAINING, null);

        assertThat(result.items()).hasSize(1);
        assertThat(result.hasNext()).isFalse();
        assertThat(result.nextCursor()).isNotBlank();
    }

    @Test
    @DisplayName("Should return null nextCursor when no response")
    void execute_WhenNoResponse_ReturnsNullCursor() {
        when(roomService.searchByCursor(any(), any(), any(), any(), any(), any(), any(Integer.class))).thenReturn(List.of());

        CursorPage<FindRoomsResponse> result = useCase.execute(
                RoomRelation.RECRUITING, null, null, null, RoomSort.CREATED_AT, null
        );

        assertThat(result.items()).isEmpty();
        assertThat(result.nextCursor()).isNull();
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("Should decode cursor when cursor is provided")
    void execute_WithCursor_UsesDecodedCursorPath() {
        List<FindRoomsResponse> responses = List.of(room("r1", 2, 1, LocalDateTime.now()));
        when(roomService.searchByCursor(any(), any(), any(), any(), any(), any(), any(Integer.class))).thenReturn(responses);

        String cursor = com.project.dorumdorum.global.pagination.CursorCodec.encode(LocalDateTime.now(), "r1");
        CursorPage<FindRoomsResponse> result = useCase.execute(
                RoomRelation.RECRUITING, null, null, null, RoomSort.CREATED_AT, cursor
        );

        assertThat(result.items()).hasSize(1);
        verify(roomService).searchByCursor(any(), any(), any(), any(), any(), any(), any(Integer.class));
    }
}
