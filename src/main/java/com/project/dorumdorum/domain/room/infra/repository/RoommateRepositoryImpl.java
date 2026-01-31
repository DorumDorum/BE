package com.project.dorumdorum.domain.room.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.room.domain.repository.RoommateRepositoryCustom;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.dorumdorum.domain.room.domain.entity.QRoommate.roommate;
import static com.project.dorumdorum.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class RoommateRepositoryImpl implements RoommateRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<MyRoommateResponse> findMyRoommates(Long userNo) {
        return query
                .select(
                        constructor(MyRoommateResponse.class,
                                roommate.roommateNo,
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
                .leftJoin(user).on(user.userNo.eq(roommate.userNo))
                .where(
                        roommate.room.in(
                                JPAExpressions
                                        .select(roommate.room)
                                        .from(roommate)
                                        .where(roommate.userNo.eq(userNo))
                        )
                )
                .fetch();
    }
}
