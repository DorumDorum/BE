package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.domain.notification.application.mapper.NotificationMapper;
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
