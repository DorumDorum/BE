package com.project.dorumdorum.domain.roommate.application.usecase;

import com.project.dorumdorum.domain.roommate.application.dto.response.RoommateHistoryResponse;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMyRoommateHistoryUseCase {

    private final RoommateService roommateService;

    @Transactional(readOnly = true)
    public List<RoommateHistoryResponse> execute(String userNo) {
        return roommateService.findMyRoommateHistory(userNo);
    }
}
