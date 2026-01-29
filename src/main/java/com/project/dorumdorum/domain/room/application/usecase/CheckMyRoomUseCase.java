package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.CheckMyRoomResponse;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckMyRoomUseCase {

    private final RoommateService roommateService;

    public CheckMyRoomResponse execute(Long userNo) {
        CheckMyRoomResponse checkMyRoomResponse = roommateService.findByUserNo(userNo)
                .map(roommate -> new CheckMyRoomResponse(
                        true,
                        String.valueOf(roommate.getRoom().getRoomNo())
                ))
                .orElseGet(() -> new CheckMyRoomResponse(false, null));

        return checkMyRoomResponse;
    }
}
