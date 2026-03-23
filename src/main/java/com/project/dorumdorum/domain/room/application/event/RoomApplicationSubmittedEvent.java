package com.project.dorumdorum.domain.room.application.event;

/** 유저가 방에 지원 완료 → 지원자-방장 1:1 채팅방 생성 트리거 */
public record RoomApplicationSubmittedEvent(
        String roomNo,
        String applicantUserNo,
        String hostUserNo
) {
}
