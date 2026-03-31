package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CancelRoomApplicationUseCase {

    private final RoomService roomService;
    private final RoomRequestService roomRequestService;

    /**
     * 방 지원 취소
     * - 방 정보를 조회
     * - 사용자의 해당 방 지원 요청을 취소
     */
    public void execute(String userNo, String roomNo) {
        Room room = roomService.findById(roomNo);
        roomRequestService.cancelJoinRequest(userNo, room);
    }
}
