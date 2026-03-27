package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.domain.notification.mapper.NotificationMapper;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.global.pagination.CursorCodec;
import com.project.dorumdorum.global.pagination.CursorPage;
import com.project.dorumdorum.global.pagination.CursorQueryParams;
import com.project.dorumdorum.global.pagination.PaginationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMyNotificationsUseCase {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private static final int LIMIT = 20;

    /**
     * 내 알림 목록 커서 조회
     * - 커서 기반 조회 조건을 생성
     * - 알림 목록을 조회해 응답 DTO로 변환
     * - 다음 페이지 커서를 포함해 반환
     */
    @Transactional(readOnly = true)
    public CursorPage<LoadNotificationResponse> execute(String userNo, String cursor) {
        CursorQueryParams params = PaginationHelper.prepareCursorQuery(cursor, LIMIT);

        List<LoadNotificationResponse> items = notificationMapper.toResponseList(
                notificationService.searchByCursor(
                        userNo,
                        params.cursorCreatedAt(),
                        params.cursorId(),
                        params.limitPlusOne()
                )
        );

        return PaginationHelper.buildCursorPage(
                items,
                LIMIT,
                last -> CursorCodec.encode(last.createdAt(), last.notificationNo())
        );
    }
}
