package com.project.dorumdorum.domain.chat.infra.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.repository.ChatMessageQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.domain.chat.domain.entity.QChatMessage.chatMessage;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageQueryRepository {

    private final JPAQueryFactory query;

    /**
     * 특정 채팅방의 메시지를 cursor 기반으로 페이징하여 반환
     */
    @Override
    public List<ChatMessage> findMessages(String chatRoomNo, LocalDateTime joinedAt,
                                          LocalDateTime cursorCreatedAt, String cursorMessageNo,
                                          int limitPlusOne) {
        return query
                .selectFrom(chatMessage)
                .where(
                        chatMessage.chatRoom.chatRoomNo.eq(chatRoomNo),
                        chatMessage.createdAt.goe(joinedAt),                    // 입장 시점 이후 메시지만
                        cursorPredicate(cursorCreatedAt, cursorMessageNo)       // 커서 이전 메시지만 (null이면 조건 없음)
                )
                .orderBy(chatMessage.createdAt.desc(), chatMessage.messageNo.desc())
                .limit(limitPlusOne)
                .fetch();
    }

    /**
     * ORDER BY (createdAt DESC, messageNo DESC) 에 대응하는 복합 커서 조건
     * (createdAt < cursor.createdAt) OR (createdAt = cursor.createdAt AND messageNo < cursor.messageNo)
     */
    private BooleanExpression cursorPredicate(LocalDateTime cursorCreatedAt, String cursorMessageNo) {
        if (cursorMessageNo == null || cursorCreatedAt == null) return null;
        return chatMessage.createdAt.lt(cursorCreatedAt)
                .or(chatMessage.createdAt.eq(cursorCreatedAt)
                        .and(chatMessage.messageNo.lt(cursorMessageNo)));
    }
}
