package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.notification.mapper.NotificationMapper;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadMyNotificationsUseCase 단위 테스트")
class LoadMyNotificationsUseCaseTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private LoadMyNotificationsUseCase useCase;

    private LoadNotificationResponse response(String no, LocalDateTime createdAt) {
        return LoadNotificationResponse.builder()
                .notificationNo(no)
                .title("t")
                .body("b")
                .type(NotificationType.ROOM_APPLICATION_APPROVED)
                .isRead(false)
                .relatedId("r1")
                .redirectPath("/path")
                .createdAt(createdAt)
                .build();
    }

    @Test
    @DisplayName("페이지 크기보다 많은 결과가 있으면 hasNext=true 와 nextCursor를 반환한다")
    void execute_WhenOverLimit_ReturnsHasNextAndCursor() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<LoadNotificationResponse> responses = java.util.stream.IntStream.rangeClosed(1, 21)
                .mapToObj(i -> response("n" + i, now.minusMinutes(i)))
                .toList();

        when(notificationService.searchByCursor(any(), any(), any(), anyInt()))
                .thenReturn(List.of()); // 실제 매핑 전 단계에선 엔티티지만 여기선 매퍼 결과만 중요
        when(notificationMapper.toResponseList(any())).thenReturn(responses);

        // when
        CursorPage<LoadNotificationResponse> page = useCase.execute("user-1", null);

        // then
        assertThat(page.items()).hasSize(20);
        assertThat(page.hasNext()).isTrue();
        assertThat(page.nextCursor()).isNotBlank();
    }

    @Test
    @DisplayName("cursor가 주어지면 CursorCodec으로 디코딩된 값으로 조회한다")
    void execute_WithCursor_DecodesAndPassesToService() {
        // given
        LocalDateTime createdAt = LocalDateTime.now();
        String cursor = com.project.dorumdorum.global.pagination.CursorCodec.encode(createdAt, "n1");

        when(notificationService.searchByCursor(any(), any(), any(), anyInt()))
                .thenReturn(List.of());
        when(notificationMapper.toResponseList(any())).thenReturn(List.of());

        // when
        useCase.execute("user-1", cursor);

        // then
        verify(notificationService).searchByCursor(
                any(), // userNo
                any(), // createdAt
                any(), // cursorId
                anyInt()
        );
    }
}

