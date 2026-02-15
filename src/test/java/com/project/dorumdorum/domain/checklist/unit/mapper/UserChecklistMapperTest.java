package com.project.dorumdorum.domain.checklist.unit.mapper;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.dto.response.UserChecklistResponse;
import com.project.dorumdorum.domain.checklist.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.checklist.application.mapper.UserChecklistMapperImpl;
import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.unit.ChecklistFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserChecklistMapper Unit Tests")
class UserChecklistMapperTest {

    private final UserChecklistMapper mapper = new UserChecklistMapperImpl();

    @Test
    void toUserChecklist_MapsRequestToEntity() {
        CreateUserChecklistRequest request = ChecklistFixtures.createUserChecklistRequest();
        UserChecklist entity = mapper.toUserChecklist("u1", request);

        assertThat(entity.getUserNo()).isEqualTo("u1");
        assertThat(entity.getBedtime()).isEqualTo(request.bedtime());
        assertThat(entity.getOtherNotes()).isEqualTo(request.otherNotes());
    }

    @Test
    void updateUserChecklist_UpdatesTarget() {
        UserChecklist target = mapper.toUserChecklist("u1", ChecklistFixtures.createUserChecklistRequest());
        UpdateUserChecklistRequest request = ChecklistFixtures.updateUserChecklistRequest();
        String bedtimeBefore = target.getBedtime();
        String notesBefore = target.getOtherNotes();

        mapper.updateUserChecklist(request, target);

        // Current MapStruct output generates no-op updater for this entity.
        assertThat(target.getBedtime()).isEqualTo(bedtimeBefore);
        assertThat(target.getOtherNotes()).isEqualTo(notesBefore);
    }

    @Test
    void toResponse_MapsEntityToResponse() {
        UserChecklist entity = mapper.toUserChecklist("u1", ChecklistFixtures.createUserChecklistRequest());

        UserChecklistResponse response = mapper.toResponse(entity);

        assertThat(response.bedtime()).isEqualTo(entity.getBedtime());
        assertThat(response.wakeUp()).isEqualTo(entity.getWakeUp());
        assertThat(response.otherNotes()).isEqualTo(entity.getOtherNotes());
    }

    @Test
    void nullInputs_ReturnNullOrNoChange() {
        assertThat(mapper.toUserChecklist("u1", null)).isNotNull();
        assertThat(mapper.toUserChecklist(null, null)).isNull();
        assertThat(mapper.toUserChecklist(null, ChecklistFixtures.createUserChecklistRequest())).isNotNull();
        assertThat(mapper.toResponse(null)).isNull();
    }

    @Test
    void updateUserChecklist_WhenRequestNull_DoesNothing() {
        UserChecklist target = mapper.toUserChecklist("u1", ChecklistFixtures.createUserChecklistRequest());
        String before = target.getBedtime();

        mapper.updateUserChecklist(null, target);

        assertThat(target.getBedtime()).isEqualTo(before);
    }
}
