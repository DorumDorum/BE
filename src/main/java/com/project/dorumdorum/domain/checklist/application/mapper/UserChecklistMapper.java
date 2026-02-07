package com.project.dorumdorum.domain.checklist.application.mapper;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.dto.response.UserChecklistResponse;
import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserChecklistMapper {

    @Mapping(target = "userChecklistNo", ignore = true)
    @Mapping(target = "userNo", source = "userNo")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserChecklist toUserChecklist(String userNo, CreateUserChecklistRequest request);

    @Mapping(target = "userChecklistNo", ignore = true)
    @Mapping(target = "userNo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserChecklist(UpdateUserChecklistRequest request, @MappingTarget UserChecklist checklist);

    UserChecklistResponse toResponse(UserChecklist checklist);
}
