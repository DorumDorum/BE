package com.project.dorumdorum.domain.room.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.dorumdorum.domain.room.domain.entity.QRoom.room;
import static com.project.dorumdorum.domain.room.domain.entity.QRoomLike.roomLike;
import static com.project.dorumdorum.domain.room.domain.entity.QRoomRequest.roomRequest;
import static com.project.dorumdorum.domain.roommate.domain.entity.QRoommate.roommate;
import static com.project.dorumdorum.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<FindRoomsResponse> findByCursor(RoomRelation relation,
                                                List<RoomType> types,
                                                List<Integer> capacities,
                                                List<ResidencePeriod> residencePeriods,
                                                RoomSort sort,
                                                LocalDateTime cursorCreatedAt,
                                                String cursorId,
                                                int limitPlusOne) {
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
                        cursorPredicate(cursorCreatedAt, cursorId)
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

    @Override
    public List<FindRoomsResponse> findLikedRooms(String userNo) {
        return query
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
                .from(roomLike)
                .join(roomLike.room, room)
                .leftJoin(user).on(user.userNo.eq(room.hostUserNo))
                .where(
                        roomLike.userNo.eq(userNo),
                        room.roomStatus.eq(RoomStatus.CONFIRM_PENDING)
                )
                .orderBy(room.createdAt.desc(), room.roomNo.desc())
                .fetch();
    }

    @Override
    public List<FindRoomsResponse> findAppliedRooms(String userNo) {
        return query
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
                .from(roomRequest)
                .join(roomRequest.room, room)
                .leftJoin(user).on(user.userNo.eq(room.hostUserNo))
                .where(
                        roomRequest.userNo.eq(userNo),
                        roomRequest.direction.eq(Direction.USER_TO_ROOM),
                        room.roomStatus.eq(RoomStatus.CONFIRM_PENDING)
                )
                .orderBy(room.createdAt.desc(), room.roomNo.desc())
                .fetch();
    }

    /** 커서는 항상 (createdAt, pk). 정렬은 sort 파라미터로 ORDER BY에서만 적용 */
    private BooleanExpression cursorPredicate(LocalDateTime cursorCreatedAt, String cursorId) {
        if (cursorCreatedAt == null || cursorId == null) return null;
        return room.createdAt.lt(cursorCreatedAt)
                .or(room.createdAt.eq(cursorCreatedAt).and(room.roomNo.lt(cursorId)));
    }
}
