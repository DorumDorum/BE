package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMyRoommatesUseCase {

    private final RoommateService roommateService;

    public List<MyRoommateResponse> execute(Long userNo) {
        return roommateService.findMyRoommates(userNo);
    }
}
