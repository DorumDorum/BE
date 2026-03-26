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
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(true);
        when(roomRuleService.findByRoomNo("r1")).thenReturn(roomRule);

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
        when(roomService.findById("r1")).thenReturn(room);
        when(roommateService.isHost("u1", room)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("u1", "r1", ChecklistFixtures.updateRoomRuleRequest()))
                .isInstanceOf(RestApiException.class);

        verify(roomRuleService, never()).findByRoomNo(anyString());
    }
}
