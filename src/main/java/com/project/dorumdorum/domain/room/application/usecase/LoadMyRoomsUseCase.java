package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadMyRoomsUseCase {

    private final RoomService roomService;

    /**
     * 내가 속한 방 조회
     * - 사용자가 현재 속한 방 정보를 조회
     * - 방 상세 요약 응답으로 반환
     */
    public FindRoomsResponse execute(String userNo) {
        return roomService.findMyRoom(userNo);
    }
}
