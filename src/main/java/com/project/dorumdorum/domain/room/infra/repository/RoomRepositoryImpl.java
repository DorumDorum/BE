package com.project.dorumdorum.domain.room.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.repository.RoomQueryRepository;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private final EntityManager entityManager;

    @Override
    public List<FindRoomsResponse> findByCursor(
            Gender gender,
            ChecklistFilterRequest request,
            LocalDateTime cursorCreatedAt,
            String cursorId,
            Integer cursorRemaining,
            int limitPlusOne
    ) {
        if (hasChecklistFilters(request)) {
            return findByCursorWithLateral(gender, request, cursorCreatedAt, cursorId, cursorRemaining, limitPlusOne);
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
                                room.residencePeriod.stringValue(),
                                room.remaining
                        )
                )
                .from(room)
                .leftJoin(user).on(user.userNo.eq(room.hostUserNo))
                .where(
                        room.roomStatus.eq(RoomStatus.CONFIRM_PENDING),
                        room.deletedAt.isNull(),
                        room.gender.eq(gender),
                        eqRoomType(request),
                        eqResidencePeriod(request),
                        eqCapacity(request),
                        request.sortType() == ChecklistFilterRequest.SortType.REMAINING
                                ? remainingCursorPredicate(cursorRemaining, cursorCreatedAt, cursorId)
                                : cursorPredicate(cursorCreatedAt, cursorId)
                );

        if (request.sortType() == ChecklistFilterRequest.SortType.REMAINING) {
            q.orderBy(room.remaining.asc(), room.createdAt.desc(), room.roomNo.desc());
        } else {
            q.orderBy(room.createdAt.desc(), room.roomNo.desc());
        }

        return q.limit(limitPlusOne).fetch();
    }

    private List<FindRoomsResponse> findByCursorWithLateral(
            Gender gender,
            ChecklistFilterRequest request,
            LocalDateTime cursorCreatedAt,
            String cursorId,
            Integer cursorRemaining,
            int limitPlusOne
    ) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    r.room_no,
                    r.room_type,
                    r.capacity,
                    r.current_mate_count,
                    r.created_at,
                    r.title,
                    u.nickname,
                    r.room_status,
                    r.residence_period::text,
                    r.remaining
                FROM room r
                JOIN LATERAL (
                    SELECT 1
                    FROM room_rule rr
                    WHERE rr.room_no = r.room_no
                """);

        Map<String, Object> params = new LinkedHashMap<>();

        appendCondition(sql, params, "rr.bedtime", "bedtime", request.bedtime());
        appendCondition(sql, params, "rr.wake_up", "wakeUp", request.wakeUp());
        appendCondition(sql, params, "rr.return_home", "returnHome", enumName(request.returnHome()));
        appendCondition(sql, params, "rr.return_home_time", "returnHomeTime", request.returnHomeTime());
        appendCondition(sql, params, "rr.cleaning", "cleaning", enumName(request.cleaning()));
        appendCondition(sql, params, "rr.phone_call", "phoneCall", enumName(request.phoneCall()));
        appendCondition(sql, params, "rr.sleep_light", "sleepLight", enumName(request.sleepLight()));
        appendCondition(sql, params, "rr.sleep_habit", "sleepHabit", enumName(request.sleepHabit()));
        appendCondition(sql, params, "rr.snoring", "snoring", enumName(request.snoring()));
        appendCondition(sql, params, "rr.shower_time", "showerTime", enumName(request.showerTime()));
        appendCondition(sql, params, "rr.eating", "eating", enumName(request.eating()));
        appendCondition(sql, params, "rr.lights_out", "lightsOut", enumName(request.lightsOut()));
        appendCondition(sql, params, "rr.lights_out_time", "lightsOutTime", request.lightsOutTime());
        appendCondition(sql, params, "rr.home_visit", "homeVisit", enumName(request.homeVisit()));
        appendCondition(sql, params, "rr.smoking", "smoking", enumName(request.smoking()));
        appendCondition(sql, params, "rr.refrigerator", "refrigerator", enumName(request.refrigerator()));
        appendCondition(sql, params, "rr.hair_dryer", "hairDryer", request.hairDryer());
        appendCondition(sql, params, "rr.alarm", "alarm", enumName(request.alarm()));
        appendCondition(sql, params, "rr.earphone", "earphone", enumName(request.earphone()));
        appendCondition(sql, params, "rr.keyskin", "keyskin", enumName(request.keyskin()));
        appendCondition(sql, params, "rr.heat", "heat", enumName(request.heat()));
        appendCondition(sql, params, "rr.cold", "cold", enumName(request.cold()));
        appendCondition(sql, params, "rr.study", "study", enumName(request.study()));
        appendCondition(sql, params, "rr.trash_can", "trashCan", enumName(request.trashCan()));

        sql.append("""
                        LIMIT 1
                    ) rule_match ON TRUE
                LEFT JOIN users u
                    ON u.user_no = r.host_user_no
                WHERE r.room_status = :roomStatus
                  AND r.deleted_at IS NULL
                  AND r.gender = :gender
                """);
        params.put("roomStatus", RoomStatus.CONFIRM_PENDING.name());
        params.put("gender", gender.name());

        appendCondition(sql, params, "r.room_type", "roomType", enumName(request.roomType()));
        appendCondition(sql, params, "r.residence_period", "residencePeriod", enumName(request.residencePeriod()));
        appendCondition(sql, params, "r.capacity", "capacity", request.capacity());
        appendCursorCondition(sql, params, request.sortType(), cursorCreatedAt, cursorId, cursorRemaining);

        sql.append("\n                ORDER BY ");
        appendOrderBy(sql, "r", request.sortType());
        sql.append("\n                LIMIT :limitPlusOne");
        params.put("limitPlusOne", limitPlusOne);

        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        params.forEach(nativeQuery::setParameter);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = nativeQuery.getResultList();

        return rows.stream()
                .map(this::toFindRoomsResponse)
                .toList();
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
                                room.residencePeriod.stringValue(),
                                room.remaining
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
                                room.residencePeriod.stringValue(),
                                room.remaining
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
                                room.residencePeriod.stringValue(),
                                room.remaining
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

    private BooleanExpression cursorPredicate(LocalDateTime cursorCreatedAt, String cursorId) {
        if (cursorCreatedAt == null || cursorId == null) return null;
        return room.createdAt.lt(cursorCreatedAt)
                .or(room.createdAt.eq(cursorCreatedAt).and(room.roomNo.lt(cursorId)));
    }

    private BooleanExpression remainingCursorPredicate(Integer cursorRemaining, LocalDateTime cursorCreatedAt, String cursorId) {
        if (cursorRemaining == null || cursorCreatedAt == null || cursorId == null) return null;
        return room.remaining.gt(cursorRemaining)
                .or(room.remaining.eq(cursorRemaining).and(room.createdAt.lt(cursorCreatedAt)))
                .or(room.remaining.eq(cursorRemaining).and(room.createdAt.eq(cursorCreatedAt)).and(room.roomNo.lt(cursorId)));
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

    private boolean hasChecklistFilters(ChecklistFilterRequest request) {
        return !isBlank(request.bedtime())
                || !isBlank(request.wakeUp())
                || request.returnHome() != null
                || !isBlank(request.returnHomeTime())
                || request.cleaning() != null
                || request.phoneCall() != null
                || request.sleepLight() != null
                || request.sleepHabit() != null
                || request.snoring() != null
                || request.showerTime() != null
                || request.eating() != null
                || request.lightsOut() != null
                || !isBlank(request.lightsOutTime())
                || request.homeVisit() != null
                || request.smoking() != null
                || request.refrigerator() != null
                || !isBlank(request.hairDryer())
                || request.alarm() != null
                || request.earphone() != null
                || request.keyskin() != null
                || request.heat() != null
                || request.cold() != null
                || request.study() != null
                || request.trashCan() != null;
    }

    private void appendCondition(
            StringBuilder sql,
            Map<String, Object> params,
            String column,
            String parameterName,
            Object value
    ) {
        if (value == null || (value instanceof String stringValue && isBlank(stringValue))) {
            return;
        }

        sql.append("\n          AND ").append(column).append(" = :").append(parameterName);
        params.put(parameterName, value);
    }

    private void appendCursorCondition(
            StringBuilder sql,
            Map<String, Object> params,
            ChecklistFilterRequest.SortType sortType,
            LocalDateTime cursorCreatedAt,
            String cursorId,
            Integer cursorRemaining
    ) {
        if (sortType == ChecklistFilterRequest.SortType.REMAINING) {
            if (cursorRemaining == null || cursorCreatedAt == null || cursorId == null) {
                return;
            }

            sql.append("""
                    
                      AND (
                          r.remaining > :cursorRemaining
                          OR (r.remaining = :cursorRemaining AND r.created_at < :cursorCreatedAt)
                          OR (r.remaining = :cursorRemaining AND r.created_at = :cursorCreatedAt AND r.room_no < :cursorId)
                      )
                    """);
            params.put("cursorRemaining", cursorRemaining);
            params.put("cursorCreatedAt", cursorCreatedAt);
            params.put("cursorId", cursorId);
            return;
        }

        if (cursorCreatedAt == null || cursorId == null) {
            return;
        }

        sql.append("""
                
                  AND (
                      r.created_at < :cursorCreatedAt
                      OR (r.created_at = :cursorCreatedAt AND r.room_no < :cursorId)
                  )
                """);
        params.put("cursorCreatedAt", cursorCreatedAt);
        params.put("cursorId", cursorId);
    }

    private void appendOrderBy(StringBuilder sql, String alias, ChecklistFilterRequest.SortType sortType) {
        if (sortType == ChecklistFilterRequest.SortType.REMAINING) {
            sql.append(alias).append(".remaining ASC, ")
                    .append(alias).append(".created_at DESC, ")
                    .append(alias).append(".room_no DESC");
            return;
        }

        sql.append(alias).append(".created_at DESC, ")
                .append(alias).append(".room_no DESC");
    }

    private FindRoomsResponse toFindRoomsResponse(Object[] row) {
        return new FindRoomsResponse(
                stringValue(row[0]),
                enumValue(RoomType.class, row[1]),
                intValue(row[2]),
                intValue(row[3]),
                localDateTimeValue(row[4]),
                stringValue(row[5]),
                stringValue(row[6]),
                enumValue(RoomStatus.class, row[7]),
                stringValue(row[8]),
                intValue(row[9])
        );
    }

    private <T extends Enum<T>> T enumValue(Class<T> enumType, Object value) {
        return value == null ? null : Enum.valueOf(enumType, value.toString());
    }

    private String enumName(Enum<?> value) {
        return value == null ? null : value.name();
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private Integer intValue(Object value) {
        return value == null ? null : ((Number) value).intValue();
    }

    private LocalDateTime localDateTimeValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        return LocalDateTime.parse(value.toString().replace(' ', 'T'));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
