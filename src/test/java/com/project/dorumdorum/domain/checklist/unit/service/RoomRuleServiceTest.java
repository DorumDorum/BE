package com.project.dorumdorum.domain.checklist.unit.service;

import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.repository.RoomRuleRepository;
import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomRuleService Unit Tests")
class RoomRuleServiceTest {

    @Mock private RoomRuleRepository roomRuleRepository;
    @InjectMocks private RoomRuleService service;

    @Test
    void save_ReturnsSavedRule() {
        RoomRule roomRule = RoomRule.builder().room(Room.builder().roomNo("r1").build()).build();
        when(roomRuleRepository.save(roomRule)).thenReturn(roomRule);
        assertThat(service.save(roomRule)).isEqualTo(roomRule);
    }

    @Test
    void findByRoomNo_WhenExists_ReturnsRule() {
        RoomRule roomRule = RoomRule.builder().room(Room.builder().roomNo("r1").build()).build();
        when(roomRuleRepository.findByRoomNo("r1")).thenReturn(Optional.of(roomRule));
        assertThat(service.findByRoomNo("r1")).isEqualTo(roomRule);
    }

    @Test
    void findByRoomNo_WhenMissing_Throws() {
        when(roomRuleRepository.findByRoomNo("r1")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findByRoomNo("r1")).isInstanceOf(RestApiException.class);
    }
}
