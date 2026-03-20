package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoomConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class CreateChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;

    /**
     * 방 전원 확정 완료(RoomConfirmedEvent) 수신 → 채팅방 생성 및 전원 입장
     * - 채팅방 없으면 생성 후 미입장 멤버 전원 입장
     * - 채팅방 있으면 아직 미입장인 멤버만 추가 (RoommateAcceptedEvent로 일부 이미 입장했을 수 있음)
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoomConfirmedEvent event) {
        ChatRoom chatRoom = chatRoomService.findByRoomNo(event.roomNo())
                .orElseGet(() -> chatRoomService.create(event.roomNo()));

        event.allMemberUserNos().stream()
                .filter(userNo -> !chatRoomMemberService.isMember(chatRoom, userNo))
                .forEach(userNo -> chatRoomMemberService.join(chatRoom, userNo));
    }
}
