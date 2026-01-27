package com.project.dorumdorum.domain.chat.infra.repository;

import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessageRoomResponse;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.repository.MessageRoomRepositoryCustom;
import com.project.dorumdorum.global.pagination.DecodedCursor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.domain.chat.domain.entity.QMessageRequest.messageRequest;
import static com.project.dorumdorum.domain.chat.domain.entity.QMessageRoom.messageRoom;
import static com.project.dorumdorum.domain.chat.domain.entity.QParticipant.participant;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class MessageRoomRepositoryImpl implements MessageRoomRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<LoadMessageRoomResponse> findByCursor(Long userNo, DecodedCursor cursor, int limitPlusOne) {
        JPAQuery<LoadMessageRoomResponse> q = query
            .select(
                constructor(LoadMessageRoomResponse.class,
                    messageRoom.messageRoomNo,
                    messageRoom.roomType,
                    messageRoom.roomStatus,
                    new CaseBuilder()
                        .when(messageRoom.roomStatus.eq(MessageRoomStatus.REQUESTED))
                        .then(messageRequest.messageRequestNo)
                        .otherwise((Long) null),
                    messageRoom.lastMessage,
                    messageRoom.lastMessageAt,
                    new CaseBuilder()
                        .when(messageRequest.senderNo.eq(userNo)).then(true)
                        .otherwise(false)
                )
            )
            .from(participant)
            .join(messageRoom).on(messageRoom.messageRoomNo.eq(participant.messageRoomNo))
            .leftJoin(messageRequest).on(messageRequest.messageRoomNo.eq(messageRoom.messageRoomNo))
            .where(
                participant.user.userNo.eq(userNo),
                participant.deletedAt.isNull(),
                messageRoom.roomStatus.ne(MessageRoomStatus.DELETED),
                cursorPredicate(cursor)
            )
            .orderBy(messageRoom.lastMessageAt.desc(), messageRoom.messageRoomNo.desc());

        return q.limit(limitPlusOne).fetch();
    }

    private BooleanExpression cursorPredicate(DecodedCursor c) {
        if (c == null) return null;
        LocalDateTime t = c.createdAt();
        Long pk = c.pk();
        return messageRoom.lastMessageAt.lt(t)
            .or(messageRoom.lastMessageAt.eq(t).and(messageRoom.messageRoomNo.lt(pk)));
    }
}
