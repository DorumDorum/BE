package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoommateKickedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RoommateKickedEventListener {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;

    /**
     * 룸메이트 강퇴(RoommateKickedEvent) → 채팅방에서 퇴장 처리
     * 발행: KickRoommateUseCase (room 도메인 담당자)
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoommateKickedEvent event) {
        chatRoomService.findByRoomNo(event.roomNo()).ifPresent(chatRoom -> {
            if (chatRoomMemberService.isMember(chatRoom, event.kickedUserNo())) {
                chatRoomMemberService.leave(
                        chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, event.kickedUserNo())
                );
                chatMessageService.save(chatRoom, "SYSTEM", "룸메이트가 퇴장했습니다.", MessageType.SYSTEM, 0);
            }
        });
    }
}
