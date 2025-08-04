package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.InviteRoomRequest;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteRoomRequestUseCase {

    private final UserService userService;

    public void execute(Long userNo, Long roomNo, Long toUser, InviteRoomRequest request) {
        userService.validateExistsById(userNo);

    }
}
