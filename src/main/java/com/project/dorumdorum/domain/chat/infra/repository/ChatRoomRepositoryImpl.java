package com.project.dorumdorum.domain.chat.infra.repository;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.domain.entity.QChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.QChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomQueryRepository;
import com.project.dorumdorum.domain.room.domain.entity.QRoom;
import com.project.dorumdorum.domain.user.domain.entity.QUser;
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
     * 내가 참여 중인 채팅방 목록을 반환 (GROUP + DIRECT 모두 포함)
     * - chatRoomType: GROUP / DIRECT 구분
     * - partnerUserNo: DIRECT 채팅방의 상대방 userNo, GROUP이면 null
     * - partnerNickname: DIRECT 채팅방의 상대방 닉네임, GROUP이면 null
     * - roomName: Room.title (GROUP/DIRECT 공통)
     * - unreadCount: 내가 읽지 않은 메시지 수
     */
    @Override
    public List<ChatRoomSummary> findMyChatRooms(String userNo) {
        QChatMessage unreadMsg = new QChatMessage("unreadMsg");
        QChatRoomMember partnerMember = new QChatRoomMember("pm");
        QChatRoomMember partnerMemberForNickname = new QChatRoomMember("pmn");
        QUser partnerUser = new QUser("partnerUser");
        QRoom qRoom = new QRoom("qRoom");

        return query
                .select(constructor(ChatRoomSummary.class,
                        chatRoom.chatRoomNo,
                        chatRoom.roomNo,
                        chatRoom.chatRoomType,
                        // DIRECT이면 상대방 userNo, GROUP이면 null
                        select(partnerMember.userNo)
                                .from(partnerMember)
                                .where(
                                        partnerMember.chatRoom.eq(chatRoom),
                                        partnerMember.userNo.ne(userNo)
                                )
                                .limit(1),
                        // DIRECT이면 상대방 닉네임, GROUP이면 null
                        select(partnerUser.nickname)
                                .from(partnerUser)
                                .where(
                                        partnerUser.userNo.eq(
                                                select(partnerMemberForNickname.userNo)
                                                        .from(partnerMemberForNickname)
                                                        .where(
                                                                partnerMemberForNickname.chatRoom.eq(chatRoom),
                                                                partnerMemberForNickname.userNo.ne(userNo)
                                                        )
                                                        .limit(1)
                                        )
                                ),
                        // 방 제목 (Room.title)
                        select(qRoom.title)
                                .from(qRoom)
                                .where(qRoom.roomNo.eq(chatRoom.roomNo)),
                        chatRoom.lastMessageContent,
                        chatRoom.lastMessageAt,
                        // 내가 읽지 않은 메시지 수 (내가 보낸 메시지 제외)
                        select(unreadMsg.count())
                                .from(unreadMsg)
                                .where(
                                        unreadMsg.chatRoom.eq(chatRoom),
                                        unreadMsg.senderNo.ne(userNo),
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
