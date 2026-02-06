package com.project.dorumdorum.domain.checklist.domain.service;

import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.repository.RoomRuleRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomRuleService {

    private final RoomRuleRepository roomRuleRepository;

    public RoomRule save(RoomRule roomRule) {
        return roomRuleRepository.save(roomRule);
    }

    public RoomRule findByRoomNo(String roomNo) {
        return roomRuleRepository.findByRoomNo(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}
