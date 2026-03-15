package com.project.dorumdorum.domain.notification.infra.repository;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutboxStatus;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationOutboxQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.domain.notification.domain.entity.QNotificationOutbox.notificationOutbox;

@Repository
@RequiredArgsConstructor
public class NotificationOutboxRepositoryImpl implements NotificationOutboxQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NotificationOutbox> findRetryableByStatus(NotificationOutboxStatus status, LocalDateTime now, Pageable pageable) {
        return queryFactory
                .selectFrom(notificationOutbox)
                .where(
                        notificationOutbox.status.eq(status),
                        notificationOutbox.retryCount.gt(0),
                        notificationOutbox.nextRetryAt.loe(now)
                )
                .orderBy(notificationOutbox.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
