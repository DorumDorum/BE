package com.project.dorumdorum.domain.room.application.mapper;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.room.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomRuleMapper {

    // DTO -> Entity (Create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomNo", source = "roomNo")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    RoomRule toRoomRule(Long roomNo, RoomCreateRequest.CreateRoomRuleRequest request);

    RoomRule.CategoryData toCategoryData(RoomCreateRequest.CreateCategoryRequest request);
    RoomRule.RuleItemData toRuleItemData(RoomCreateRequest.CreateRuleItemRequest request);
    RoomRule.RuleOptionData toRuleOptionData(RoomCreateRequest.CreateRuleOptionRequest request);

    // DTO -> Entity (Update)
    RoomRule.CategoryData toCategoryData(UpdateRoomRuleRequest.UpdateCategoryRequest request);
    RoomRule.RuleItemData toRuleItemData(UpdateRoomRuleRequest.UpdateRuleItemRequest request);
    RoomRule.RuleOptionData toRuleOptionData(UpdateRoomRuleRequest.UpdateRuleOptionRequest request);

    // Entity -> DTO
    @Mapping(target = "otherNotes", source = "otherNotes")
    @Mapping(target = "categories", source = "categories")
    MyRoomRuleResponse toResponse(RoomRule roomRule);

    MyRoomRuleResponse.CategoryResponse toCategoryResponse(RoomRule.CategoryData category);
    MyRoomRuleResponse.RuleItemResponse toRuleItemResponse(RoomRule.RuleItemData item);
    MyRoomRuleResponse.RuleOptionResponse toRuleOptionResponse(RoomRule.RuleOptionData option);
}
