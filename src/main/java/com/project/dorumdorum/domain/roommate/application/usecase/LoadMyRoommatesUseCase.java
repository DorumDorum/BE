package com.project.dorumdorum.domain.roommate.application.usecase;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMyRoommatesUseCase {

    private final RoommateService roommateService;

    /**
     * 내 룸메이트 목록 조회
     * - 사용자가 속한 방 기준 룸메이트 목록을 조회
     * - 응답 DTO 목록으로 반환
     */
    public List<MyRoommateResponse> execute(String userNo) {
        return roommateService.findMyRoommates(userNo);
    }
}
