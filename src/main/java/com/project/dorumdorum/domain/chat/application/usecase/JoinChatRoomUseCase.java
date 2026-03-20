package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoommateAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class JoinChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;

    /**
     * 방장이 룸메이트 승인(RoommateAcceptedEvent) → 채팅방 입장
     * - 채팅방 없으면 생성 후 방장 + 신규 멤버 입장
     * - 채팅방 있으면 신규 멤버만 입장 (중복 방지)
     * - 입장 시스템 메시지 저장
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoommateAcceptedEvent event) {
        ChatRoom chatRoom = chatRoomService.findByRoomNo(event.roomNo())
                .orElseGet(() -> {
                    ChatRoom created = chatRoomService.create(event.roomNo());
                    chatRoomMemberService.join(created, event.hostUserNo());
                    return created;
                });

        if (!chatRoomMemberService.isMember(chatRoom, event.acceptedUserNo())) {
            chatRoomMemberService.join(chatRoom, event.acceptedUserNo());
            chatMessageService.save(chatRoom, "SYSTEM", "새 룸메이트가 입장했습니다.", MessageType.SYSTEM, 0);
        }
    }
}
