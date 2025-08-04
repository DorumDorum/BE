package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteRequestDecisionUseCase {

    private final UserService userService;
    private final RoomService roomService;
    private final RoommateService roommateService;

    public void approve(Long userNo, Long roomRequestNo, Long roomNo) {
        userService.validateExistsById(userNo);

        Room room = roomService.findById(roomNo);

        // 모레 이어서 개발해둘테니 여긴 신경쓰지 말아줘요 .. 졸려서 뇌사왔네요
    }
}
