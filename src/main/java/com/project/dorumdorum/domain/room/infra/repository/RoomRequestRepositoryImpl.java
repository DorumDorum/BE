package com.project.dorumdorum.domain.room.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.dorumdorum.domain.room.domain.entity.QRoomRequest.roomRequest;
import static com.project.dorumdorum.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class RoomRequestRepositoryImpl implements RoomRequestQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<RoomRequestApplicationResponse> findApplicationsByRoom(Room room) {
        return query
                .select(
                        constructor(RoomRequestApplicationResponse.class,
                                roomRequest.roomRequestNo,
                                roomRequest.userNo,
                                user.name,
                                user.nickname,
                                user.major,
                                user.studentNo,
                                user.grade,
                                user.age,
                                roomRequest.createdAt,
                                roomRequest.introduction,
                                roomRequest.additionalMessage
                        )
                )
                .from(roomRequest)
                .leftJoin(user).on(user.userNo.eq(roomRequest.userNo))
                .where(roomRequest.room.eq(room))
                .orderBy(roomRequest.createdAt.desc())
                .fetch();
    }
}
