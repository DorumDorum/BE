package com.project.dorumdorum.domain.roommate.infra.repository;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.domain.entity.QRoommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.dorumdorum.domain.roommate.domain.entity.QRoommate.roommate;
import static com.project.dorumdorum.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class RoommateRepositoryImpl implements RoommateQueryRepository {

    private final JPAQueryFactory query;

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
}
