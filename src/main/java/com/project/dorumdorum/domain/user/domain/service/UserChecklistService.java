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
