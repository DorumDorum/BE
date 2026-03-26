package com.project.dorumdorum.domain.room.application.event;

import java.util.List;

/** 방 전원 확정 완료 → 채팅방 생성 트리거 */
public record RoomConfirmedEvent(
        String roomNo,
        List<String> allMemberUserNos
) {
}
