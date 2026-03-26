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
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoadChatMessagesUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private static final int LIMIT = 30;

    @Transactional(readOnly = true)
    public CursorPage<ChatMessageSummary> execute(String chatRoomNo, String userNo, String cursor) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomNo(chatRoomNo);
        ChatRoomMember member = chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, userNo);

        CursorQueryParams params = PaginationHelper.prepareCursorQuery(cursor, LIMIT);

        List<ChatMessage> messages = chatMessageService
                .findMessages(chatRoomNo, member.getJoinedAt(), params.cursorCreatedAt(), params.cursorId(), params.limitPlusOne());

        Set<String> senderNos = messages.stream()
                .map(ChatMessage::getSenderNo)
                .collect(Collectors.toSet());

        Map<String, String> nicknameMap = new HashMap<>();
        for (String sNo : senderNos) {
            try {
                User user = userService.findById(sNo);
                String nickname = user.getNickname();
                if (nickname == null || nickname.isBlank()) {
                    nickname = user.getName();
                }
                nicknameMap.put(sNo, (nickname != null && !nickname.isBlank()) ? nickname : "알 수 없음");
            } catch (Exception e) {
                nicknameMap.put(sNo, "알 수 없음");
            }
        }

        List<ChatMessageSummary> items = messages.stream()
                .map(m -> new ChatMessageSummary(
                        m.getMessageNo(),
                        m.getSenderNo(),
                        nicknameMap.getOrDefault(m.getSenderNo(), "알 수 없음"),
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
