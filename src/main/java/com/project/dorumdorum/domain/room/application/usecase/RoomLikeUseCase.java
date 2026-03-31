package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomLikeService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomLikeUseCase {

    private final UserService userService;
    private final RoomService roomService;
    private final RoomLikeService roomLikeService;

    /**
     * 방 찜 등록
     * - 사용자와 방 존재 여부를 검증
     * - 해당 방을 찜 목록에 추가
     */
    public void like(String userNo, String roomNo) {
        userService.validateExistsById(userNo);
        Room room = roomService.findById(roomNo);

        roomLikeService.like(userNo, room);
    }

    /**
     * 방 찜 해제
     * - 사용자와 방 존재 여부를 검증
     * - 해당 방을 찜 목록에서 제거
     */
    public void unlike(String userNo, String roomNo) {
        userService.validateExistsById(userNo);
        Room room = roomService.findById(roomNo);

        roomLikeService.unlike(userNo, room);
    }
}
