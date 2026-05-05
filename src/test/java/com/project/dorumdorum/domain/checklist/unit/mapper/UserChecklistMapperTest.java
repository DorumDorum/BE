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

        mapper.updateUserChecklist(request, target);

        assertThat(target.getBedtime()).isEqualTo(request.bedtime());
        assertThat(target.getWakeUp()).isEqualTo(request.wakeUp());
        assertThat(target.getReturnHome()).isEqualTo(request.returnHome());
        assertThat(target.getReturnHomeTime()).isEqualTo(request.returnHomeTime());
        assertThat(target.getCleaning()).isEqualTo(request.cleaning());
        assertThat(target.getPhoneCall()).isEqualTo(request.phoneCall());
        assertThat(target.getSleepLight()).isEqualTo(request.sleepLight());
        assertThat(target.getSleepHabit()).isEqualTo(request.sleepHabit());
        assertThat(target.getSnoring()).isEqualTo(request.snoring());
        assertThat(target.getShowerTime()).isEqualTo(request.showerTime());
        assertThat(target.getEating()).isEqualTo(request.eating());
        assertThat(target.getLightsOut()).isEqualTo(request.lightsOut());
        assertThat(target.getLightsOutTime()).isEqualTo(request.lightsOutTime());
        assertThat(target.getHomeVisit()).isEqualTo(request.homeVisit());
        assertThat(target.getSmoking()).isEqualTo(request.smoking());
        assertThat(target.getRefrigerator()).isEqualTo(request.refrigerator());
        assertThat(target.getHairDryer()).isEqualTo(request.hairDryer());
        assertThat(target.getAlarm()).isEqualTo(request.alarm());
        assertThat(target.getEarphone()).isEqualTo(request.earphone());
        assertThat(target.getKeyskin()).isEqualTo(request.keyskin());
        assertThat(target.getHeat()).isEqualTo(request.heat());
        assertThat(target.getCold()).isEqualTo(request.cold());
        assertThat(target.getStudy()).isEqualTo(request.study());
        assertThat(target.getTrashCan()).isEqualTo(request.trashCan());
        assertThat(target.getOtherNotes()).isEqualTo(request.otherNotes());
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
