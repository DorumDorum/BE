package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadMyLikedRoomsUseCase {

    private final RoomService roomService;

    /**
     * 내가 찜한 방 목록 조회
     * - 사용자가 찜한 방 목록을 조회
     * - 방 목록 응답 DTO로 반환
     */
    public List<FindRoomsResponse> execute(String userNo) {
        return roomService.findLikedRooms(userNo);
    }
}
