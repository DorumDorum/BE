package com.project.dorumdorum.domain.checklist.application.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.domain.service.UserChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateUserChecklistUseCase {

    private final UserChecklistService userChecklistService;
    private final UserChecklistMapper userChecklistMapper;

    /**
     * 사용자 체크리스트 수정
     * - 기존 체크리스트를 조회
     * - 요청값으로 항목을 갱신한 뒤 저장
     */
    public void execute(String userNo, UpdateUserChecklistRequest request) {
        UserChecklist checklist = userChecklistService.findByUserNo(userNo);
        userChecklistMapper.updateUserChecklist(request, checklist);
        userChecklistService.save(checklist);
    }
}
