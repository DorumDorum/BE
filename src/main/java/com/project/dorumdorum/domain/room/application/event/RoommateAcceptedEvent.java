package com.project.dorumdorum.domain.room.application.event;

/** 방장이 룸메이트 지원 승인 → 채팅방 입장 트리거 */
public record RoommateAcceptedEvent(
        String roomNo,
        String acceptedUserNo,
        String hostUserNo
) {
}
