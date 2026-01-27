package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import com.project.dorumdorum.domain.room.domain.repository.RoomRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class RoomRuleService {

    private final RoomRuleRepository roomRuleRepository;
    private final RoomRuleMapper roomRuleMapper;

    public RoomRule create(Long roomNo, RoomCreateRequest.CreateRoomRuleRequest request) {
        RoomRule document = roomRuleMapper.toRoomRule(roomNo, request);
        return roomRuleRepository.save(document);
    }

    public RoomRule findByRoomNo(Long roomNo) {
        return roomRuleRepository.findByRoomNo(roomNo)
                .orElse(null);
    }
}
