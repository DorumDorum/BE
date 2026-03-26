package com.project.dorumdorum.domain.room.application.event;

/**
 * 룸메이트 강퇴 → 채팅방 퇴장 트리거
 * 발행 책임: KickRoommateUseCase (room 도메인 담당자)
 */
public record RoommateKickedEvent(
        String roomNo,
        String kickedUserNo
) {
}
