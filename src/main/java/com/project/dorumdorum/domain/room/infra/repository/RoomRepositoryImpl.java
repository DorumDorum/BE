package com.project.dorumdorum.domain.room.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.LoadRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.RoomRole;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepositoryCustom;
import com.project.dorumdorum.global.pagination.DecodedCursor;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.domain.room.domain.entity.QRoom.room;
import static com.project.dorumdorum.domain.room.domain.entity.QRoomRequest.roomRequest;
import static com.project.dorumdorum.domain.room.domain.entity.QRoommate.roommate;
import static com.project.dorumdorum.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<LoadRoomsResponse> findByCursor(Long userNo,
                                          RoomRelation relation,
                                          List<Tag> tags,
                                          RoomType type,
                                          Integer capacity,
                                          RoomSort sort,
                                          DecodedCursor cursor,
                                          int limitPlusOne) {
        JPAQuery<LoadRoomsResponse> q = query
                .select(
                        constructor(LoadRoomsResponse.class,
                                room.roomNo,
                                room.roomType,
                                room.capacity,
                                room.currentMateCount,
                                room.createdAt,
                                room.title,
                                user.nickname,
                                room.tags
                        )
                )
                .from(room)
                .leftJoin(user).on(user.userNo.eq(room.hostUserNo))
                .where(
                        relationPredicate(userNo, relation),
                        type == null ? null : room.roomType.eq(type),
                        capacity == null ? null : room.capacity.eq(capacity),
                        tagsAnyPredicate(tags),
                        cursorPredicate(cursor, sort)
                );

        if (sort == RoomSort.REMAINING) {
            q.orderBy(room.remaining.asc(), room.createdAt.desc(), room.roomNo.desc());
        } else {
            q.orderBy(room.createdAt.desc(), room.roomNo.desc());
        }

        return q.limit(limitPlusOne).fetch();
    }

    private BooleanExpression relationPredicate(Long userNo, RoomRelation relation) {
        if (relation == null) return null;
        return switch (relation) {
            case APPLIED -> room.roomNo.in(
                    query.select(roomRequest.room.roomNo)
                            .from(roomRequest)
                            .where(roomRequest.userNo.eq(userNo))
            );
            case JOINED -> room.roomNo.in(
                    query.select(roommate.room.roomNo)
                            .from(roommate)
                            .where(roommate.userNo.eq(userNo))
            );
            case RECRUITING -> room.roomStatus.eq(RoomStatus.CONFIRM_PENDING);
        };
    }

    // ANY 매칭: 요청 태그 이름 중 하나라도 포함
    private BooleanExpression tagsAnyPredicate(List<Tag> tags) {
        if (tags == null || tags.isEmpty())
            return null;

        return room.tags.any().in(tags);
    }

    private BooleanExpression cursorPredicate(DecodedCursor c, RoomSort sort) {
        if (c == null) return null;
        LocalDateTime t = c.createdAt();
        Long pk = c.pk();

        if (sort == RoomSort.REMAINING) {
            Integer r = c.remaining();
            return room.remaining.gt(r)
                    .or(room.remaining.eq(r).and(room.createdAt.lt(t)))
                    .or(room.remaining.eq(r).and(room.createdAt.eq(t)).and(room.roomNo.lt(pk)));
        } else {
            return room.createdAt.lt(t)
                    .or(room.createdAt.eq(t).and(room.roomNo.lt(pk)));
        }
    }
}
