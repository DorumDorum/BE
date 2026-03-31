package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoomConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;

    /**
     * 방 전원 확정 완료(RoomConfirmedEvent) 수신 → 채팅방 생성 및 전원 입장
     * - 채팅방 없으면 생성 후 미입장 멤버 전원 입장
     * - 채팅방 있으면 아직 미입장인 멤버만 추가 (RoommateAcceptedEvent로 일부 이미 입장했을 수 있음)
     *
     * BEFORE_COMMIT: 부모 트랜잭션(ConfirmRoomAssignmentUseCase)에 참여.
     * 채팅방 생성 실패 시 방 확정도 롤백 → 원자성 보장.
     * DataIntegrityViolationException: 동시 요청으로 인한 중복 생성/입장 시 재조회로 처리.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RoomConfirmedEvent event) {
        ChatRoom chatRoom = findOrCreate(event.roomNo());

        event.allMemberUserNos().stream()
                .filter(userNo -> !chatRoomMemberService.isMember(chatRoom, userNo))
                .forEach(userNo -> joinSafely(chatRoom, userNo));
    }

    private ChatRoom findOrCreate(String roomNo) {
        return chatRoomService.findByRoomNo(roomNo).orElseGet(() -> {
            try {
                return chatRoomService.create(roomNo);
            } catch (DataIntegrityViolationException e) {
                log.debug("[Chat] 채팅방 중복 생성 (동시 요청). roomNo={}", roomNo);
                return chatRoomService.findByRoomNo(roomNo)
                        .orElseThrow(() -> e);
            }
        });
    }

    private void joinSafely(ChatRoom chatRoom, String userNo) {
        try {
            chatRoomMemberService.join(chatRoom, userNo);
        } catch (DataIntegrityViolationException e) {
            log.debug("[Chat] 채팅방 중복 입장 (동시 요청). chatRoomNo={}, userNo={}",
                    chatRoom.getChatRoomNo(), userNo);
        }
    }
}
