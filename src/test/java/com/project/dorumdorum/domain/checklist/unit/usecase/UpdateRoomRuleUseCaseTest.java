package com.project.dorumdorum.domain.checklist.unit.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.checklist.application.usecase.UpdateRoomRuleUseCase;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.checklist.unit.ChecklistFixtures;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.INVALID_ROOM_CAPACITY;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateRoomRuleUseCase Unit Tests")
class UpdateRoomRuleUseCaseTest {

    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @Mock private RoomRuleService roomRuleService;
    @Mock private RoomRuleMapper roomRuleMapper;
    @InjectMocks private UpdateRoomRuleUseCase useCase;

    @Test
    void execute_WhenHost_UpdatesRoomAndRule() {
        UpdateRoomRuleRequest request = ChecklistFixtures.updateRoomRuleRequest();
        Room room = mock(Room.class);
        RoomRule roomRule = RoomRule.builder().room(Room.builder().roomNo("r1").build()).build();
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(true);
        when(roomRuleService.findByRoomNo("r1")).thenReturn(roomRule);
        when(room.isValidCapacity(request.capacity())).thenReturn(true);

        useCase.execute("u1", "r1", request);

        verify(room).updateCapacity(request.capacity());
        verify(room).updateRoomType(request.roomType());
        verify(room).updateResidencePeriod(request.residencePeriod());
        verify(roomRuleMapper).updateRoomRule(request, roomRule);
        verify(roomRuleService).save(roomRule);
    }

    @Test
    void execute_WhenNotHost_Throws() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("u1", "r1", ChecklistFixtures.updateRoomRuleRequest()))
                .isInstanceOf(RestApiException.class);

        verify(roomRuleService, never()).findByRoomNo(anyString());
    }

    @Test
    void execute_WhenCapacityLessThanCurrentMateCount_Throws() {
        UpdateRoomRuleRequest request = ChecklistFixtures.updateRoomRuleRequest();
        Room room = spy(Room.builder()
                .roomNo("r1")
                .capacity(3)
                .title("title")
                .hostUserNo("host")
                .roomType(request.roomType())
                .residencePeriod(request.residencePeriod())
                .build());
        room.init();
        room.plusCurrentMate();
        RoomRule roomRule = RoomRule.builder().room(room).build();
        UpdateRoomRuleRequest invalidRequest = new UpdateRoomRuleRequest(
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
                request.otherNotes(),
                request.roomType(),
                1,
                request.residencePeriod()
        );

        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(true);
        when(roomRuleService.findByRoomNo("r1")).thenReturn(roomRule);

        assertThatThrownBy(() -> useCase.execute("u1", "r1", invalidRequest))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode.code")
                .isEqualTo(INVALID_ROOM_CAPACITY.getCode().getCode());

        verify(roomRuleMapper, never()).updateRoomRule(any(), any());
        verify(roomRuleService, never()).save(any());
    }
}
