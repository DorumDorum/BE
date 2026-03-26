package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.usecase.CreateRoomUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateRoomUseCase Unit Tests")
class CreateRoomUseCaseTest {

    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @Mock private RoomRuleService roomRuleService;
    @Mock private RoomRuleMapper roomRuleMapper;
    @InjectMocks private CreateRoomUseCase useCase;

    @Test
    @DisplayName("Should create room, host roommate and room rule")
    void execute_CreatesRoomAndHostAndRule() {
        String userNo = "u1";
        CreateRoomRuleRequest ruleRequest = org.mockito.Mockito.mock(CreateRoomRuleRequest.class);
        RoomCreateRequest request = new RoomCreateRequest(RoomType.TYPE_1, 2, ResidencePeriod.SEMESTER, "title", ruleRequest);
        Room room = Room.builder().roomNo("r1").build();
        RoomRule roomRule = org.mockito.Mockito.mock(RoomRule.class);

        when(roomService.create(userNo, request)).thenReturn(room);
        when(roomRuleMapper.toRoomRule(room, ruleRequest)).thenReturn(roomRule);

        useCase.execute(userNo, request);

        verify(roomService).create(userNo, request);
        verify(roommateService).create(userNo, room, RoomRole.HOST);
        verify(roomRuleMapper).toRoomRule(room, ruleRequest);
        verify(roomRuleService).save(roomRule);
    }
}
