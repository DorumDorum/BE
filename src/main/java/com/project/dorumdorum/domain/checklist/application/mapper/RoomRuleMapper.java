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
    void updateRoomRule(UpdateRoomRuleRequest request, @MappingTarget RoomRule roomRule);

    MyRoomRuleResponse toResponse(RoomRule roomRule);
}
