package com.project.dorumdorum.domain.roommate.infra.repository;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.application.dto.response.RoommateHistoryResponse;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.roommate.domain.entity.QRoommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.domain.roommate.domain.entity.QRoommate.roommate;
import static com.project.dorumdorum.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Projections.constructor;

@Repository
public class RoommateRepositoryImpl implements RoommateQueryRepository {

    private final JPAQueryFactory query;
    private final EntityManager entityManager;

    @Autowired
    public RoommateRepositoryImpl(JPAQueryFactory query, EntityManager entityManager) {
        this.query = query;
        this.entityManager = entityManager;
    }

    public RoommateRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
        this.entityManager = null;
    }

    @Override
    public List<MyRoommateResponse> findMyRoommates(String userNo) {
        QRoommate myRoommate = new QRoommate("myRoommate");

        return query
                .select(
                        constructor(MyRoommateResponse.class,
                                roommate.roommateNo,
                                roommate.userNo,
                                roommate.confirmStatus,
                                roommate.roomRole,
                                user.name,
                                user.nickname,
                                user.studentNo,
                                user.major,
                                user.grade,
                                user.age,
                                user.gender,
                                roommate.userNo.eq(userNo)
                        )
                )
                .from(roommate)
                .join(myRoommate).on(
                        myRoommate.room.eq(roommate.room)
                                .and(myRoommate.userNo.eq(userNo))
                                .and(myRoommate.deletedAt.isNull())
                )
                .leftJoin(user).on(user.userNo.eq(roommate.userNo))
                .where(
                        roommate.deletedAt.isNull(),
                        roommate.room.deletedAt.isNull()
                )
                .fetch();
    }

    @Override
    public List<RoommateHistoryResponse> findMyRoommateHistory(String userNo) {
        Query nativeQuery = entityManager.createNativeQuery("""
                SELECT
                    rm.roommate_no,
                    r.room_no,
                    r.title,
                    r.room_type,
                    r.capacity,
                    rm.user_no,
                    u.name,
                    u.nickname,
                    u.student_no,
                    u.major,
                    SUBSTRING(u.student_no FROM 3 FOR 2),
                    GREATEST(COALESCE(my_rm.created_at, r.created_at), COALESCE(r.created_at, my_rm.created_at)),
                    CASE
                        WHEN my_rm.deleted_at IS NULL AND rm.deleted_at IS NULL AND r.deleted_at IS NULL THEN NULL
                        ELSE COALESCE(rm.deleted_at, my_rm.deleted_at, r.deleted_at)
                    END,
                    CASE
                        WHEN my_rm.deleted_at IS NULL AND rm.deleted_at IS NULL AND r.deleted_at IS NULL THEN 'CURRENT'
                        ELSE 'PAST'
                    END
                FROM roommate my_rm
                JOIN roommate rm
                    ON rm.room_no = my_rm.room_no
                   AND rm.user_no <> :userNo
                JOIN room r
                    ON r.room_no = my_rm.room_no
                LEFT JOIN users u
                    ON u.user_no = rm.user_no
                WHERE my_rm.user_no = :userNo
                ORDER BY
                    CASE
                        WHEN my_rm.deleted_at IS NULL AND rm.deleted_at IS NULL AND r.deleted_at IS NULL THEN 0
                        ELSE 1
                    END,
                    COALESCE(rm.deleted_at, my_rm.deleted_at, r.deleted_at, rm.created_at) DESC,
                    rm.roommate_no DESC
                """);
        nativeQuery.setParameter("userNo", userNo);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = nativeQuery.getResultList();
        return rows.stream().map(this::toRoommateHistoryResponse).toList();
    }

    private RoommateHistoryResponse toRoommateHistoryResponse(Object[] row) {
        return new RoommateHistoryResponse(
                stringValue(row[0]),
                stringValue(row[1]),
                stringValue(row[2]),
                enumValue(RoomType.class, row[3]),
                intValue(row[4]),
                stringValue(row[5]),
                stringValue(row[6]),
                stringValue(row[7]),
                stringValue(row[8]),
                stringValue(row[9]),
                stringValue(row[10]),
                localDateTimeValue(row[11]),
                localDateTimeValue(row[12]),
                enumValue(RoommateHistoryResponse.Relation.class, row[13])
        );
    }

    private <T extends Enum<T>> T enumValue(Class<T> enumType, Object value) {
        return value == null ? null : Enum.valueOf(enumType, value.toString());
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
}
