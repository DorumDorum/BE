package com.project.dorumdorum.domain.checklist.domain.service;

import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.domain.repository.UserChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserChecklistService {

    private final UserChecklistRepository userChecklistRepository;

    public UserChecklist findByUserNo(String userNo) {
        return userChecklistRepository.findByUserNo(userNo)
                .orElseGet(() ->
                        UserChecklist.builder()
                                .userNo(userNo)
                                .otherNotes("")
                                .build()
                );
    }

    public UserChecklist save(UserChecklist checklist) {
        return userChecklistRepository.save(checklist);
    }
}
