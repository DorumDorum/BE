package com.project.dorumdorum.domain.user.application.mapper;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.user.application.dto.response.MyUserChecklistResponse;
import com.project.dorumdorum.domain.user.domain.entity.UserChecklist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserChecklistMapper {

    // Entity -> DTO
    @Mapping(target = "otherNotes", source = "otherNotes")
    @Mapping(target = "categories", source = "categories")
    MyUserChecklistResponse toResponse(UserChecklist checklist);

    MyUserChecklistResponse.CategoryResponse toCategoryResponse(UserChecklist.CategoryData category);
    MyUserChecklistResponse.RuleItemResponse toRuleItemResponse(UserChecklist.RuleItemData item);
    MyUserChecklistResponse.RuleOptionResponse toRuleOptionResponse(UserChecklist.RuleOptionData option);

    // DTO -> Entity (Update)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userNo", source = "userNo")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    UserChecklist toUserChecklist(Long userNo, UpdateUserChecklistRequest request);

    UserChecklist.CategoryData toCategoryData(UpdateUserChecklistRequest.UpdateCategoryRequest request);
    UserChecklist.RuleItemData toRuleItemData(UpdateUserChecklistRequest.UpdateRuleItemRequest request);
    UserChecklist.RuleOptionData toRuleOptionData(UpdateUserChecklistRequest.UpdateRuleOptionRequest request);
}
