package com.project.dorumdorum.domain.notification.infra.repository;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.domain.notification.domain.entity.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationJpaRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> searchByCursor(String recipientNo, LocalDateTime cursorCreatedAt, String cursorId, int limitPlusOne) {
        return queryFactory
                .selectFrom(notification)
                .where(
                        notification.recipientNo.eq(recipientNo),
                        notification.deletedAt.isNull(),
                        cursorPredicate(cursorCreatedAt, cursorId)
                )
                .orderBy(notification.createdAt.desc(), notification.notificationNo.desc())
                .limit(limitPlusOne)
                .fetch();
    }

    private BooleanExpression cursorPredicate(LocalDateTime cursorCreatedAt, String cursorId) {
        if (cursorCreatedAt == null || cursorId == null) {
            return null;
        }
        return notification.createdAt.lt(cursorCreatedAt)
                .or(notification.createdAt.eq(cursorCreatedAt).and(notification.notificationNo.lt(cursorId)));
    }
}
