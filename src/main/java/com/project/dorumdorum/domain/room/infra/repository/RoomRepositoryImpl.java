package com.project.dorumdorum.domain.room.infra.repository;

import com.project.dorumdorum.domain.checklist.domain.entity.QRoomRule;
import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
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
    private static final QRoomRule roomRule = QRoomRule.roomRule;

    @Override
    public List<FindRoomsResponse> findByCursor(
            ChecklistFilterRequest request,
            LocalDateTime cursorCreatedAt,
            String cursorId,
            int limitPlusOne
    ) {
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
                .leftJoin(roomRule).on(roomRule.roomNo.eq(room.roomNo))
                .leftJoin(user).on(user.userNo.eq(room.hostUserNo))
                .where(
                        room.roomStatus.eq(RoomStatus.CONFIRM_PENDING),
                        eqRoomType(request),
                        eqResidencePeriod(request),
                        eqCapacity(request),
                        eqBedtime(request),
                        eqWakeUp(request),
                        eqReturnHome(request),
                        eqReturnHomeTime(request),
                        eqCleaning(request),
                        eqPhoneCall(request),
                        eqSleepLight(request),
                        eqSleepHabit(request),
                        eqSnoring(request),
                        eqShowerTime(request),
                        eqEating(request),
                        eqLightsOut(request),
                        eqLightsOutTime(request),
                        eqHomeVisit(request),
                        eqSmoking(request),
                        eqRefrigerator(request),
                        eqHairDryer(request),
                        eqAlarm(request),
                        eqEarphone(request),
                        eqKeyskin(request),
                        eqHeat(request),
                        eqCold(request),
                        eqStudy(request),
                        eqTrashCan(request),
                        cursorPredicate(cursorCreatedAt, cursorId)
                );

        if (request.sortType() == ChecklistFilterRequest.SortType.REMAINING) {
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

    private BooleanExpression eqRoomType(ChecklistFilterRequest request) {
        return request.roomType() == null ? null : room.roomType.eq(request.roomType());
    }

    private BooleanExpression eqResidencePeriod(ChecklistFilterRequest request) {
        return request.residencePeriod() == null ? null : room.residencePeriod.eq(request.residencePeriod());
    }

    private BooleanExpression eqCapacity(ChecklistFilterRequest request) {
        return request.capacity() == null ? null : room.capacity.eq(request.capacity());
    }

    private BooleanExpression eqBedtime(ChecklistFilterRequest request) {
        return isBlank(request.bedtime()) ? null : roomRule.bedtime.eq(request.bedtime());
    }

    private BooleanExpression eqWakeUp(ChecklistFilterRequest request) {
        return isBlank(request.wakeUp()) ? null : roomRule.wakeUp.eq(request.wakeUp());
    }

    private BooleanExpression eqReturnHome(ChecklistFilterRequest request) {
        return request.returnHome() == null ? null : roomRule.returnHome.eq(request.returnHome());
    }

    private BooleanExpression eqReturnHomeTime(ChecklistFilterRequest request) {
        return isBlank(request.returnHomeTime()) ? null : roomRule.returnHomeTime.eq(request.returnHomeTime());
    }

    private BooleanExpression eqCleaning(ChecklistFilterRequest request) {
        return request.cleaning() == null ? null : roomRule.cleaning.eq(request.cleaning());
    }

    private BooleanExpression eqPhoneCall(ChecklistFilterRequest request) {
        return request.phoneCall() == null ? null : roomRule.phoneCall.eq(request.phoneCall());
    }

    private BooleanExpression eqSleepLight(ChecklistFilterRequest request) {
        return request.sleepLight() == null ? null : roomRule.sleepLight.eq(request.sleepLight());
    }

    private BooleanExpression eqSleepHabit(ChecklistFilterRequest request) {
        return request.sleepHabit() == null ? null : roomRule.sleepHabit.eq(request.sleepHabit());
    }

    private BooleanExpression eqSnoring(ChecklistFilterRequest request) {
        return request.snoring() == null ? null : roomRule.snoring.eq(request.snoring());
    }

    private BooleanExpression eqShowerTime(ChecklistFilterRequest request) {
        return request.showerTime() == null ? null : roomRule.showerTime.eq(request.showerTime());
    }

    private BooleanExpression eqEating(ChecklistFilterRequest request) {
        return request.eating() == null ? null : roomRule.eating.eq(request.eating());
    }

    private BooleanExpression eqLightsOut(ChecklistFilterRequest request) {
        return request.lightsOut() == null ? null : roomRule.lightsOut.eq(request.lightsOut());
    }

    private BooleanExpression eqLightsOutTime(ChecklistFilterRequest request) {
        return isBlank(request.lightsOutTime()) ? null : roomRule.lightsOutTime.eq(request.lightsOutTime());
    }

    private BooleanExpression eqHomeVisit(ChecklistFilterRequest request) {
        return request.homeVisit() == null ? null : roomRule.homeVisit.eq(request.homeVisit());
    }

    private BooleanExpression eqSmoking(ChecklistFilterRequest request) {
        return request.smoking() == null ? null : roomRule.smoking.eq(request.smoking());
    }

    private BooleanExpression eqRefrigerator(ChecklistFilterRequest request) {
        return request.refrigerator() == null ? null : roomRule.refrigerator.eq(request.refrigerator());
    }

    private BooleanExpression eqHairDryer(ChecklistFilterRequest request) {
        return isBlank(request.hairDryer()) ? null : roomRule.hairDryer.eq(request.hairDryer());
    }

    private BooleanExpression eqAlarm(ChecklistFilterRequest request) {
        return request.alarm() == null ? null : roomRule.alarm.eq(request.alarm());
    }

    private BooleanExpression eqEarphone(ChecklistFilterRequest request) {
        return request.earphone() == null ? null : roomRule.earphone.eq(request.earphone());
    }

    private BooleanExpression eqKeyskin(ChecklistFilterRequest request) {
        return request.keyskin() == null ? null : roomRule.keyskin.eq(request.keyskin());
    }

    private BooleanExpression eqHeat(ChecklistFilterRequest request) {
        return request.heat() == null ? null : roomRule.heat.eq(request.heat());
    }

    private BooleanExpression eqCold(ChecklistFilterRequest request) {
        return request.cold() == null ? null : roomRule.cold.eq(request.cold());
    }

    private BooleanExpression eqStudy(ChecklistFilterRequest request) {
        return request.study() == null ? null : roomRule.study.eq(request.study());
    }

    private BooleanExpression eqTrashCan(ChecklistFilterRequest request) {
        return request.trashCan() == null ? null : roomRule.trashCan.eq(request.trashCan());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
