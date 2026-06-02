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
    @Mapping(target = "deletedAt", ignore = true)
    UserChecklist toUserChecklist(String userNo, CreateUserChecklistRequest request);

    @Mapping(target = "userChecklistNo", ignore = true)
    @Mapping(target = "userNo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    default void updateUserChecklist(UpdateUserChecklistRequest request, @MappingTarget UserChecklist checklist) {
        if (request == null || checklist == null) {
            return;
        }

        checklist.updateChecklist(
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

    UserChecklistResponse toResponse(UserChecklist checklist);
}
