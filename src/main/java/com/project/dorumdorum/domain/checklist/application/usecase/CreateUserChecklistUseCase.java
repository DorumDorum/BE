package com.project.dorumdorum.domain.checklist.application.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.domain.service.UserChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateUserChecklistUseCase {

    private final UserChecklistService userChecklistService;
    private final UserChecklistMapper userChecklistMapper;

    /**
     * 사용자 체크리스트 생성
     * - 요청값을 체크리스트 엔티티로 변환
     * - 저장 후 생성된 체크리스트 번호를 반환
     */
    public String execute(String userNo, CreateUserChecklistRequest request) {
        UserChecklist checklist = userChecklistMapper.toUserChecklist(userNo, request);
        UserChecklist savedChecklist = userChecklistService.save(checklist);
        return savedChecklist.getUserChecklistNo();
    }
}
