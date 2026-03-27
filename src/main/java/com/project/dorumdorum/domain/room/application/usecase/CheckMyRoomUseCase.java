package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.CheckMyRoomResponse;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckMyRoomUseCase {

    private final RoommateService roommateService;

    /**
     * 내가 속한 방 여부 조회
     * - 사용자 룸메이트 정보를 조회
     * - 방 소속 여부와 방 번호를 함께 반환
     */
    public CheckMyRoomResponse execute(String userNo) {
        CheckMyRoomResponse checkMyRoomResponse = roommateService.findByUserNo(userNo)
                .map(roommate -> new CheckMyRoomResponse(
                        true,
                        roommate.getRoom().getRoomNo()
                ))
                .orElseGet(() -> new CheckMyRoomResponse(false, null));

        return checkMyRoomResponse;
    }
}
