package com.project.dorumdorum.domain.checklist.application.mapper;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomRuleMapper {

    @Mapping(target = "roomRuleNo", ignore = true)
    @Mapping(target = "room", source = "room")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RoomRule toRoomRule(Room room, CreateRoomRuleRequest request);

    @Mapping(target = "roomRuleNo", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    default void updateRoomRule(UpdateRoomRuleRequest request, @MappingTarget RoomRule roomRule) {
        if (request == null || roomRule == null) {
            return;
        }

        roomRule.updateChecklist(
                request.bedtime(),
                request.wakeUp(),
                request.returnHome(),
                request.returnHomeTime(),
                request.cleaning(),
                request.phoneCall(),
                request.sleepLight(),
                request.sleepHabit(),
                request.snoring(),
                request.showerTime(),
                request.eating(),
                request.lightsOut(),
                request.lightsOutTime(),
                request.homeVisit(),
                request.smoking(),
                request.refrigerator(),
                request.hairDryer(),
                request.alarm(),
                request.earphone(),
                request.keyskin(),
                request.heat(),
                request.cold(),
                request.study(),
                request.trashCan(),
                request.otherNotes()
        );
    }

    MyRoomRuleResponse toResponse(RoomRule roomRule);
}
