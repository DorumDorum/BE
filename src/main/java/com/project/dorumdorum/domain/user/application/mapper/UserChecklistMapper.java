package com.project.dorumdorum.domain.user.application.mapper;

import com.project.dorumdorum.domain.user.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.user.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.user.application.dto.response.UserChecklistResponse;
import com.project.dorumdorum.domain.user.domain.entity.UserChecklist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserChecklistMapper {

    // Entity -> DTO
    @Mapping(target = "otherNotes", source = "otherNotes")
    @Mapping(target = "categories", source = "categories")
    UserChecklistResponse toResponse(UserChecklist checklist);

    UserChecklistResponse.CategoryResponse toCategoryResponse(UserChecklist.CategoryData category);
    UserChecklistResponse.RuleItemResponse toRuleItemResponse(UserChecklist.RuleItemData item);
    UserChecklistResponse.RuleOptionResponse toRuleOptionResponse(UserChecklist.RuleOptionData option);

    // DTO -> Entity (Create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userNo", source = "userNo")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserChecklist toUserChecklist(String userNo, CreateUserChecklistRequest request);

    UserChecklist.CategoryData toCategoryData(CreateUserChecklistRequest.CreateCategoryRequest request);
    UserChecklist.RuleItemData toRuleItemData(CreateUserChecklistRequest.CreateRuleItemRequest request);
    UserChecklist.RuleOptionData toRuleOptionData(CreateUserChecklistRequest.CreateRuleOptionRequest request);

    // DTO -> Entity (Update)
    UserChecklist.CategoryData toCategoryData(UpdateUserChecklistRequest.UpdateCategoryRequest request);
    UserChecklist.RuleItemData toRuleItemData(UpdateUserChecklistRequest.UpdateRuleItemRequest request);
    UserChecklist.RuleOptionData toRuleOptionData(UpdateUserChecklistRequest.UpdateRuleOptionRequest request);
}
