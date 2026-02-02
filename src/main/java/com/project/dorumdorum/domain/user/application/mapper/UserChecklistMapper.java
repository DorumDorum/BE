package com.project.dorumdorum.domain.user.application.mapper;

import com.project.dorumdorum.domain.user.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.user.application.dto.response.MyUserChecklistResponse;
import com.project.dorumdorum.domain.user.domain.entity.UserChecklist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

import static com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory.*;
import static com.project.dorumdorum.domain.room.domain.entity.RuleItemType.*;

@Mapper(componentModel = "spring")
public interface UserChecklistMapper {

    // Entity -> DTO
    @Mapping(target = "otherNotes", source = "otherNotes")
    @Mapping(target = "categories", source = "categories")
    MyUserChecklistResponse toResponse(UserChecklist checklist);

    MyUserChecklistResponse.CategoryResponse toCategoryResponse(UserChecklist.CategoryData category);
    MyUserChecklistResponse.RuleItemResponse toRuleItemResponse(UserChecklist.RuleItemData item);
    MyUserChecklistResponse.RuleOptionResponse toRuleOptionResponse(UserChecklist.RuleOptionData option);

    // DTO -> Entity (Create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userNo", source = "userNo")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserChecklist toUserChecklist(Long userNo, CreateUserChecklistRequest request);

    UserChecklist.CategoryData toCategoryData(CreateUserChecklistRequest.CreateCategoryRequest request);
    UserChecklist.RuleItemData toRuleItemData(CreateUserChecklistRequest.CreateRuleItemRequest request);
    UserChecklist.RuleOptionData toRuleOptionData(CreateUserChecklistRequest.CreateRuleOptionRequest request);

    // DTO -> Entity (Update)
    UserChecklist.CategoryData toCategoryData(UpdateUserChecklistRequest.UpdateCategoryRequest request);
    UserChecklist.RuleItemData toRuleItemData(UpdateUserChecklistRequest.UpdateRuleItemRequest request);
    UserChecklist.RuleOptionData toRuleOptionData(UpdateUserChecklistRequest.UpdateRuleOptionRequest request);
}
