package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.room.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import com.project.dorumdorum.domain.room.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory.BASIC_INFO;
import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRoomRuleUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;
    private final RoomRuleService roomRuleService;
    private final RoomRuleMapper roomRuleMapper;

    public void execute(Long userNo, Long roomNo, UpdateRoomRuleRequest request) {
        Room room = roomService.findById(roomNo);

        if (!roommateService.isHost(userNo, room))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        // RoomRule update
        RoomRule roomRule = roomRuleService.findByRoomNo(roomNo);

        List<RoomRule.CategoryData> categories = request.categories().stream()
                .map(roomRuleMapper::toCategoryData)
                .collect(Collectors.toList());

        // Room update
        room.updateCapacity(request.capacity());
        room.updateRoomType(request.roomType());
        room.updateResidencePeriod(request.residencePeriod());

        roomRule.updateOtherNotes(request.otherNotes());
        roomRule.updateCategories(categories);

        roomRuleService.save(roomRule);
    }
}
