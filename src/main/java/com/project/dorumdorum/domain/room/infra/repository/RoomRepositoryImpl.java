package com.project.dorumdorum.domain.room.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepositoryCustom;
import com.project.dorumdorum.global.pagination.DecodedCursor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.dorumdorum.domain.room.domain.entity.QRoom.room;
import static com.project.dorumdorum.domain.roommate.domain.entity.QRoommate.roommate;
import static com.project.dorumdorum.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<FindRoomsResponse> findByCursor(RoomRelation relation,
                                                List<RoomType> types,
                                                List<Integer> capacities,
                                                List<ResidencePeriod> residencePeriods,
                                                RoomSort sort,
                                                DecodedCursor cursor,
                                                int limitPlusOne) {
        // 공개 API: RECRUITING만 지원 (userNo 없음)
        if (relation != RoomRelation.RECRUITING) {
            return Collections.emptyList();
        }

        JPAQuery<FindRoomsResponse> q = query
                .select(
                        constructor(FindRoomsResponse.class,
                                room.roomNo,
                                room.roomType,
                                room.capacity,
                                room.currentMateCount,
                                room.createdAt,
                                room.title,
                                user.nickname,
                                room.roomStatus,
                                room.residencePeriod.stringValue()
                        )
                )
                .from(room)
                .leftJoin(user).on(user.userNo.eq(room.hostUserNo))
                .where(
                        room.roomStatus.eq(RoomStatus.CONFIRM_PENDING),
                        types == null || types.isEmpty() ? null : room.roomType.in(types),
                        capacities == null || capacities.isEmpty() ? null : room.capacity.in(capacities),
                        residencePeriods == null || residencePeriods.isEmpty() ? null : room.residencePeriod.in(residencePeriods),
                        cursorPredicate(cursor, sort)
                );

        if (sort == RoomSort.REMAINING) {
            q.orderBy(room.remaining.asc(), room.createdAt.desc(), room.roomNo.desc());
        } else {
            q.orderBy(room.createdAt.desc(), room.roomNo.desc());
        }

        return q.limit(limitPlusOne).fetch();
    }

    @Override
    public Optional<FindRoomsResponse> findMyRoom(String userNo) {
        FindRoomsResponse result = query
                .select(
                        constructor(FindRoomsResponse.class,
                                room.roomNo,
                                room.roomType,
                                room.capacity,
                                room.currentMateCount,
                                room.createdAt,
                                room.title,
                                user.nickname,
                                room.roomStatus,
                                room.residencePeriod.stringValue()
                        )
                )
                .from(room)
                .leftJoin(user).on(user.userNo.eq(room.hostUserNo))
                .where(
                        JPAExpressions
                                .selectOne()
                                .from(roommate)
                                .where(roommate.room.eq(room), roommate.userNo.eq(userNo))
                                .exists()
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression cursorPredicate(DecodedCursor c, RoomSort sort) {
        if (c == null) return null;
        LocalDateTime t = c.createdAt();
        String pk = c.pk();

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
