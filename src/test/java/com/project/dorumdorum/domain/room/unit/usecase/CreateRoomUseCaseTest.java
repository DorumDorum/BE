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
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dorumdorum.global.exception.RestApiException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateRoomUseCase Unit Tests")
class CreateRoomUseCaseTest {

    @Mock private UserService userService;
    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @Mock private RoomRuleService roomRuleService;
    @Mock private RoomRuleMapper roomRuleMapper;
    @InjectMocks private CreateRoomUseCase useCase;

    @Test
    @DisplayName("Should throw when user is already in a room")
    void execute_WhenUserAlreadyInRoom_Throws() {
        String userNo = "u1";
        CreateRoomRuleRequest ruleRequest = mock(CreateRoomRuleRequest.class);
        RoomCreateRequest request = new RoomCreateRequest(RoomType.TYPE_1, 2, ResidencePeriod.SEMESTER, "title", null, ruleRequest);

        when(roommateService.existsByUserNo(userNo)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(userNo, request))
                .isInstanceOf(RestApiException.class);

        verify(roomService, never()).create(any(), any(), any());
    }

    @Test
    @DisplayName("Should create room with host gender, host roommate and room rule")
    void execute_CreatesRoomAndHostAndRule() {
        String userNo = "u1";
        User host = User.builder().userNo(userNo).gender(Gender.MALE).build();
        CreateRoomRuleRequest ruleRequest = mock(CreateRoomRuleRequest.class);
        RoomCreateRequest request = new RoomCreateRequest(RoomType.TYPE_1, 2, ResidencePeriod.SEMESTER, "title", null, ruleRequest);
        Room room = Room.builder().roomNo("r1").gender(Gender.MALE).build();
        RoomRule roomRule = mock(RoomRule.class);

        when(roommateService.existsByUserNo(userNo)).thenReturn(false);
        when(userService.findById(userNo)).thenReturn(host);
        when(roomService.create(userNo, Gender.MALE, request)).thenReturn(room);
        when(roomRuleMapper.toRoomRule(room, ruleRequest)).thenReturn(roomRule);

        useCase.execute(userNo, request);

        verify(userService).findById(userNo);
        verify(roomService).create(userNo, Gender.MALE, request);
        verify(roommateService).create(userNo, room, RoomRole.HOST);
        verify(roomRuleMapper).toRoomRule(room, ruleRequest);
        verify(roomRuleService).save(roomRule);
    }
}
