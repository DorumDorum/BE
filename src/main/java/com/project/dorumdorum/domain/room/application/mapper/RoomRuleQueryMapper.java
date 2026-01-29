package com.project.dorumdorum.domain.room.application.mapper;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * RoomRule(Document) -> MyRoomRuleResponse(조회 DTO) 변환용 MapStruct 매퍼
 */
@Mapper(componentModel = "spring")
public interface RoomRuleQueryMapper {

    @Mapping(target = "otherNotes", source = "otherNotes")
    @Mapping(target = "categories", source = "categories")
    MyRoomRuleResponse toResponse(RoomRule roomRule);

    MyRoomRuleResponse.CategoryResponse toCategoryResponse(RoomRule.CategoryData category);

    MyRoomRuleResponse.RuleItemResponse toRuleItemResponse(RoomRule.RuleItemData item);

    MyRoomRuleResponse.RuleOptionResponse toRuleOptionResponse(RoomRule.RuleOptionData option);
}

