package com.project.dorumdorum.domain.user.domain.service;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.user.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.user.domain.repository.UserChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserChecklistService {

    private final UserChecklistRepository userChecklistRepository;
    private final UserChecklistMapper userChecklistMapper;

    public UserChecklist create(String userNo, SignUpRequest request) {
        UserChecklist checklist = userChecklistMapper.toUserChecklist(userNo, request.checklist());
        return userChecklistRepository.save(checklist);
    }

    public UserChecklist findByUserNo(String userNo) {
        return userChecklistRepository.findByUserNo(userNo)
                .orElseGet(() ->
                        UserChecklist.builder()
                                .userNo(userNo)
                                .categories(new ArrayList<>())
                                .otherNotes("")
                                .build()
                );
    }

    public UserChecklist save(UserChecklist checklist) {
        return userChecklistRepository.save(checklist);
    }
}
