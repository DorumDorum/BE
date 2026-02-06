package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.response.UserChecklistResponse;
import com.project.dorumdorum.domain.user.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.user.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.user.domain.service.UserChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadUserChecklistUseCase {

    private final UserChecklistService userChecklistService;
    private final UserChecklistMapper userChecklistMapper;

    public UserChecklistResponse execute(String userNo) {
        UserChecklist checklist = userChecklistService.findByUserNo(userNo);
        return userChecklistMapper.toResponse(checklist);
    }
}
