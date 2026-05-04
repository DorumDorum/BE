package com.project.dorumdorum.domain.checklist.unit.mapper;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapperImpl;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.unit.ChecklistFixtures;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RoomRuleMapper Unit Tests")
class RoomRuleMapperTest {

    private final RoomRuleMapper mapper = new RoomRuleMapperImpl();
    private final Room room = Room.builder().roomNo("r1").build();

    @Test
    void toRoomRule_MapsRequestToEntity() {
        CreateRoomRuleRequest request = ChecklistFixtures.createRoomRuleRequest();
        RoomRule entity = mapper.toRoomRule(room, request);

        assertThat(entity.getRoomNo()).isEqualTo("r1");
        assertThat(entity.getBedtime()).isEqualTo(request.bedtime());
        assertThat(entity.getOtherNotes()).isEqualTo(request.otherNotes());
    }

    @Test
    void updateRoomRule_UpdatesTarget() {
        RoomRule target = mapper.toRoomRule(room, ChecklistFixtures.createRoomRuleRequest());
        UpdateRoomRuleRequest request = ChecklistFixtures.updateRoomRuleRequest();

        mapper.updateRoomRule(request, target);

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
        RoomRule entity = mapper.toRoomRule(room, ChecklistFixtures.createRoomRuleRequest());

        MyRoomRuleResponse response = mapper.toResponse(entity);

        assertThat(response.bedtime()).isEqualTo(entity.getBedtime());
        assertThat(response.wakeUp()).isEqualTo(entity.getWakeUp());
        assertThat(response.otherNotes()).isEqualTo(entity.getOtherNotes());
    }

    @Test
    void nullInputs_ReturnNull() {
        assertThat(mapper.toRoomRule(room, null)).isNotNull();
        assertThat(mapper.toRoomRule(null, null)).isNull();
        assertThat(mapper.toRoomRule(null, ChecklistFixtures.createRoomRuleRequest())).isNotNull();
        assertThat(mapper.toResponse(null)).isNull();
    }

    @Test
    void updateRoomRule_WhenRequestNull_DoesNothing() {
        RoomRule target = mapper.toRoomRule(room, ChecklistFixtures.createRoomRuleRequest());
        String before = target.getBedtime();

        mapper.updateRoomRule(null, target);

        assertThat(target.getBedtime()).isEqualTo(before);
    }
}
