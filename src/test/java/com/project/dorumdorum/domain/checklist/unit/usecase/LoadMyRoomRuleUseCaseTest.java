package com.project.dorumdorum.domain.checklist.unit.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.checklist.application.usecase.LoadMyRoomRuleUseCase;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadMyRoomRuleUseCase Unit Tests")
class LoadMyRoomRuleUseCaseTest {

    @Mock private RoomRuleService roomRuleService;
    @Mock private RoomRuleMapper roomRuleMapper;
    @InjectMocks private LoadMyRoomRuleUseCase useCase;

    @Test
    void execute_LoadsAndMapsRoomRule() {
        RoomRule roomRule = RoomRule.builder().room(Room.builder().roomNo("r1").build()).build();
        MyRoomRuleResponse response = MyRoomRuleResponse.builder().bedtime("23:00").build();
        when(roomRuleService.findByRoomNo("r1")).thenReturn(roomRule);
        when(roomRuleMapper.toResponse(roomRule)).thenReturn(response);

        MyRoomRuleResponse result = useCase.execute("r1");

        assertThat(result).isEqualTo(response);
        verify(roomRuleService).findByRoomNo("r1");
        verify(roomRuleMapper).toResponse(roomRule);
    }
}
