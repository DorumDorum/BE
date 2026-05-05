package com.project.dorumdorum.domain.room.application.event;

/**
 * 방 삭제 → 채팅방 삭제 트리거
 * 발행 책임: DeleteRoomUseCase (room 도메인 담당자)
 */
public record RoomDeletedEvent(
        String roomNo
) {
}
