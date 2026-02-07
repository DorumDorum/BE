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

    public List<MyRoommateResponse> execute(String userNo) {
        return roommateService.findMyRoommates(userNo);
    }
}
