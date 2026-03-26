package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MarkChatRoomReadUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;

    @Transactional
    public void execute(String chatRoomNo, String userNo) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomNo(chatRoomNo);
        ChatRoomMember member = chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, userNo);

        LocalDateTime fromTime = member.getLastReadAt() != null
                ? member.getLastReadAt()
                : member.getJoinedAt();

        chatMessageService.decreaseUnreadCount(chatRoomNo, fromTime, userNo);
        chatRoomMemberService.updateLastReadAt(member, LocalDateTime.now());
    }
}
