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

    /**
     * 채팅방 읽음 처리
     * - 사용자의 채팅방 멤버 정보를 조회
     * - 마지막 읽은 시점 이후 메시지의 안 읽은 수를 감소
     * - 현재 시각으로 마지막 읽은 시점을 갱신
     */
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
