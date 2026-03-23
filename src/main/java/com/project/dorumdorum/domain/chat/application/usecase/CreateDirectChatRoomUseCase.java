package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.room.application.event.RoomApplicationSubmittedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateDirectChatRoomUseCase {

    private final GetOrCreateDirectChatRoomUseCase getOrCreateDirectChatRoomUseCase;

    /**
     * 방 지원(RoomApplicationSubmittedEvent) → 지원자-방장 1:1 채팅방 생성
     * - AFTER_COMMIT: 방 지원 트랜잭션 커밋 후 실행
     * - 예외가 HTTP 응답에 영향을 주지 않도록 모든 예외를 내부에서 처리
     * - 생성 실패 시 채팅 버튼 클릭 시점에 on-demand로 생성됨
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(RoomApplicationSubmittedEvent event) {
        try {
            getOrCreateDirectChatRoomUseCase.createIfAbsent(
                    event.roomNo(), event.applicantUserNo(), event.hostUserNo());
        } catch (Exception e) {
            log.warn("[DirectChat] 채팅방 사전 생성 실패 (채팅 버튼 클릭 시 생성됩니다): roomNo={}, applicantUserNo={}, error={}",
                    event.roomNo(), event.applicantUserNo(), e.getMessage());
        }
    }
}
