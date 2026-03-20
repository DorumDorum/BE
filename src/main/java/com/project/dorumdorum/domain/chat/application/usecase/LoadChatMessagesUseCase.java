package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageSummary;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.global.pagination.CursorCodec;
import com.project.dorumdorum.global.pagination.CursorPage;
import com.project.dorumdorum.global.pagination.CursorQueryParams;
import com.project.dorumdorum.global.pagination.PaginationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadChatMessagesUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private static final int LIMIT = 30;

    @Transactional(readOnly = true)
    public CursorPage<ChatMessageSummary> execute(String chatRoomNo, String userNo, String cursor) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomNo(chatRoomNo);
        ChatRoomMember member = chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, userNo);

        CursorQueryParams params = PaginationHelper.prepareCursorQuery(cursor, LIMIT);

        List<ChatMessageSummary> items = chatMessageService
                .findMessages(chatRoomNo, member.getJoinedAt(), params.cursorCreatedAt(), params.cursorId(), params.limitPlusOne())
                .stream()
                .map(m -> new ChatMessageSummary(
                        m.getMessageNo(),
                        m.getSenderNo(),
                        m.getContent(),
                        m.getMessageType().name(),
                        m.getCreatedAt(),
                        m.getUnreadCount()
                ))
                .toList();

        return PaginationHelper.buildCursorPage(
                items,
                LIMIT,
                last -> CursorCodec.encode(last.sentAt(), last.messageNo())
        );
    }
}
