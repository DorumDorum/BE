package com.project.dorumdorum.domain.notification.infra.repository;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryOutboxStatus;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutboxStatus;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeliveryOutboxQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.domain.notification.domain.entity.QNotificationDeliveryOutbox.notificationDeliveryOutbox;
import static com.project.dorumdorum.domain.notification.domain.entity.QNotificationOutbox.notificationOutbox;

@Repository
@RequiredArgsConstructor
public class NotificationDeliveryOutboxRepositoryImpl implements NotificationDeliveryOutboxQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NotificationDeliveryOutbox> findProcessableByStatus(
            NotificationDeliveryOutboxStatus status,
            NotificationOutboxStatus notificationOutboxStatus,
            LocalDateTime now,
            Pageable pageable
    ) {
        return queryFactory
                .selectFrom(notificationDeliveryOutbox)
                .join(notificationOutbox).on(notificationOutbox.outboxNo.eq(notificationDeliveryOutbox.notificationOutboxNo))
                .where(
                        notificationDeliveryOutbox.status.eq(status),
                        notificationOutbox.status.eq(notificationOutboxStatus),
                        notificationOutbox.notificationNo.isNotNull(),
                        notificationDeliveryOutbox.nextRetryAt.isNull()
                                .or(notificationDeliveryOutbox.nextRetryAt.loe(now))
                )
                .orderBy(notificationDeliveryOutbox.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
