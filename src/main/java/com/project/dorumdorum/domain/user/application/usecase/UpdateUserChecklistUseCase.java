package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.user.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.user.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.user.domain.service.UserChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateUserChecklistUseCase {

    private final UserChecklistService userChecklistService;
    private final UserChecklistMapper userChecklistMapper;

    public void execute(String userNo, UpdateUserChecklistRequest request) {
        UserChecklist checklist = userChecklistService.findByUserNo(userNo);

        List<UserChecklist.CategoryData> categories = request.categories().stream()
                .map(userChecklistMapper::toCategoryData)
                .collect(Collectors.toList());

        checklist.updateOtherNotes(request.otherNotes());
        checklist.updateCategories(categories);

        userChecklistService.save(checklist);
    }
}
