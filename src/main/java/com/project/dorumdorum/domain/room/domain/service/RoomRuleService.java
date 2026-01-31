package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.room.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import com.project.dorumdorum.domain.room.domain.repository.RoomRuleRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

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

    public RoomRule update(Long roomNo, UpdateRoomRuleRequest request) {
        RoomRule roomRule = roomRuleRepository.findByRoomNo(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        List<RoomRule.CategoryData> categories = request.categories().stream()
                .map(roomRuleMapper::toCategoryData)
                .collect(Collectors.toList());

        if (request.otherNotes() != null) {
            roomRule.updateOtherNotes(request.otherNotes());
        }
        roomRule.updateCategories(categories);

        return roomRuleRepository.save(roomRule);
    }
}
