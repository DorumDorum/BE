package com.project.dorumdorum.domain.chat.infra.repository;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.domain.entity.QChatMessage;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.dorumdorum.domain.chat.domain.entity.QChatRoom.chatRoom;
import static com.project.dorumdorum.domain.chat.domain.entity.QChatRoomMember.chatRoomMember;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomQueryRepository {

    private final JPAQueryFactory query;

    /**
     * 내가 참여 중인 채팅방 목록을 반환
     * 각 채팅방마다 안읽은 메시지 수를 서브쿼리로 계산해서 함께 반환
     */
    @Override
    public List<ChatRoomSummary> findMyChatRooms(String userNo) {
        QChatMessage unreadMsg = new QChatMessage("unreadMsg");

        return query
                .select(constructor(ChatRoomSummary.class,
                        chatRoom.chatRoomNo,
                        chatRoom.roomNo,
                        chatRoom.lastMessageContent,
                        chatRoom.lastMessageAt,
                        // 해당 채팅방에서 내가 아직 읽지 않은 메시지 수
                        select(unreadMsg.count())
                                .from(unreadMsg)
                                .where(
                                        unreadMsg.chatRoom.eq(chatRoom),
                                        unreadMsg.createdAt.goe(chatRoomMember.joinedAt),
                                        chatRoomMember.lastReadAt.isNull()
                                                .or(unreadMsg.createdAt.after(chatRoomMember.lastReadAt))
                                )
                ))
                .from(chatRoom)
                .join(chatRoomMember).on(
                        chatRoomMember.chatRoom.eq(chatRoom),
                        chatRoomMember.userNo.eq(userNo)
                )
                .orderBy(chatRoom.lastMessageAt.desc().nullsLast())
                .fetch();
    }
}
