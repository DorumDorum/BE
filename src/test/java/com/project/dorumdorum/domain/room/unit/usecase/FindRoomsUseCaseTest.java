package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsPageResponse;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.FindRoomsUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindRoomsUseCase Unit Tests")
class FindRoomsUseCaseTest {

    @Mock private UserService userService;
    @Mock private RoomService roomService;
    @InjectMocks private FindRoomsUseCase useCase;

    private static final String USER_NO = "u1";
    private static final User MALE_USER = User.builder().userNo(USER_NO).gender(Gender.MALE).build();

    @BeforeEach
    void setUp() {
        when(userService.findById(USER_NO)).thenReturn(MALE_USER);
    }

    private FindRoomsResponse room(String roomNo, int capacity, int current, LocalDateTime createdAt) {
        return new FindRoomsResponse(roomNo, RoomType.TYPE_1, capacity, current, createdAt, "title", null,
                "host", "경영", "22", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name(), capacity - current);
    }

    private ChecklistFilterRequest request(ChecklistFilterRequest.SortType sortType, String cursor) {
        return new ChecklistFilterRequest(
                sortType, cursor, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null
        );
    }

    @Test
    @DisplayName("Should return hasNext true when responses exceed limit")
    void execute_WhenResponsesExceedLimit_ReturnsHasNextTrue() {
        LocalDateTime now = LocalDateTime.now();
        List<FindRoomsResponse> responses = java.util.stream.IntStream.rangeClosed(1, 51)
                .mapToObj(i -> room("r" + i, 2, 1, now.minusMinutes(i)))
                .toList();
        ChecklistFilterRequest request = request(ChecklistFilterRequest.SortType.LATEST, null);
        when(roomService.searchByCursor(any(), any(), any(), any(), any(), anyInt()))
                .thenReturn(responses);
        when(roomService.countSearchResults(any(), any())).thenReturn(120L);

        FindRoomsPageResponse result = useCase.execute(USER_NO, request);

        assertThat(result.items()).hasSize(50);
        assertThat(result.hasNext()).isTrue();
        assertThat(result.nextCursor()).isNotBlank();
        assertThat(result.totalCount()).isEqualTo(120L);
        verify(roomService).searchByCursor(any(), any(), any(), any(), any(), anyInt());
    }

    @Test
    @DisplayName("Should return hasNext false when responses within limit")
    void execute_WhenResponsesWithinLimit_ReturnsHasNextFalse() {
        List<FindRoomsResponse> responses = List.of(room("r1", 2, 1, LocalDateTime.now()));
        ChecklistFilterRequest request = request(ChecklistFilterRequest.SortType.REMAINING, null);
        when(roomService.searchByCursor(any(), any(), any(), any(), any(), anyInt()))
                .thenReturn(responses);

        FindRoomsPageResponse result = useCase.execute(USER_NO, request);

        assertThat(result.items()).hasSize(1);
        assertThat(result.hasNext()).isFalse();
        assertThat(result.nextCursor()).isNull();
    }

    @Test
    @DisplayName("Should return null nextCursor when no response")
    void execute_WhenNoResponse_ReturnsNullCursor() {
        ChecklistFilterRequest request = request(ChecklistFilterRequest.SortType.LATEST, null);
        when(roomService.searchByCursor(any(), any(), any(), any(), any(), anyInt()))
                .thenReturn(List.of());

        FindRoomsPageResponse result = useCase.execute(USER_NO, request);

        assertThat(result.items()).isEmpty();
        assertThat(result.nextCursor()).isNull();
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("Should decode cursor when cursor is provided")
    void execute_WithCursor_UsesDecodedCursorPath() {
        List<FindRoomsResponse> responses = List.of(room("r1", 2, 1, LocalDateTime.now()));
        String cursor = com.project.dorumdorum.global.pagination.CursorCodec.encode(LocalDateTime.now(), "r1");
        ChecklistFilterRequest request = request(ChecklistFilterRequest.SortType.LATEST, cursor);
        when(roomService.searchByCursor(any(), any(), any(), any(), any(), anyInt()))
                .thenReturn(responses);

        FindRoomsPageResponse result = useCase.execute(USER_NO, request);

        assertThat(result.items()).hasSize(1);
        assertThat(result.totalCount()).isNull();
        verify(roomService).searchByCursor(any(), any(), any(), any(), any(), anyInt());
        verify(roomService, never()).countSearchResults(any(), any());
    }
}
