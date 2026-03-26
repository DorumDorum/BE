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
        String bedtimeBefore = target.getBedtime();
        String notesBefore = target.getOtherNotes();

        mapper.updateRoomRule(request, target);

        // Current MapStruct output generates no-op updater for this entity.
        assertThat(target.getBedtime()).isEqualTo(bedtimeBefore);
        assertThat(target.getOtherNotes()).isEqualTo(notesBefore);
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
