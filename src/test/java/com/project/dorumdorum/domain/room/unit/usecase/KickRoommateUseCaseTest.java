package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.event.RoommateKickedEvent;
import com.project.dorumdorum.domain.room.application.usecase.KickRoommateUseCase;
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
import org.springframework.context.ApplicationEventPublisher;

import static com.project.dorumdorum.global.exception.code.status.CommonErrorStatus._NOT_FOUND;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.CANNOT_KICK_SELF;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("KickRoommateUseCase Unit Tests")
class KickRoommateUseCaseTest {

    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @InjectMocks private KickRoommateUseCase useCase;

    @Test
    @DisplayName("방장이 다른 멤버를 강퇴하면 Roommate 삭제, 인원 감소, 이벤트 발행")
    void execute_WhenHostKicksOther_Success() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(roommateService.isHost("host", room)).thenReturn(true);

        useCase.execute("host", "r1", "member1");

        verify(roommateService).leaveRoom("member1", "r1");
        verify(room).minusCurrentMate();
        verify(roomService).flush();
        verify(eventPublisher).publishEvent(new RoommateKickedEvent("r1", "member1"));
    }

    @Test
    @DisplayName("존재하지 않는 방 ID로 요청 시 RestApiException 발생")
    void execute_WhenRoomNotFound_ThrowsException() {
        when(roomService.findByIdForUpdate("nonexistent")).thenThrow(new RestApiException(NO_PERMISSION_ON_ROOM));

        assertThatThrownBy(() -> useCase.execute("host", "nonexistent", "member1"))
                .isInstanceOf(RestApiException.class);

        verify(roommateService, never()).isHost(any(), any());
        verify(roommateService, never()).leaveRoom(any(), any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("강퇴 대상이 해당 방에 없으면 leaveRoom에서 RestApiException 발생")
    void execute_WhenKickedUserNotInRoom_ThrowsException() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(roommateService.isHost("host", room)).thenReturn(true);
        doThrow(new RestApiException(_NOT_FOUND))
                .when(roommateService).leaveRoom("ghost", "r1");

        assertThatThrownBy(() -> useCase.execute("host", "r1", "ghost"))
                .isInstanceOf(RestApiException.class);

        verify(room, never()).minusCurrentMate();
        verify(roomService, never()).flush();
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("방장이 아닌 사람이 강퇴 시도 시 NO_PERMISSION_ON_ROOM 예외")
    void execute_WhenNotHost_ThrowsNoPermission() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(roommateService.isHost("member1", room)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("member1", "r1", "member2"))
                .isInstanceOf(RestApiException.class);

        verify(roommateService, never()).leaveRoom(any(), any());
        verify(roomService, never()).flush();
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("방장이 자기 자신을 강퇴 시도 시 CANNOT_KICK_SELF 예외")
    void execute_WhenKickSelf_ThrowsCannotKickSelf() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(roommateService.isHost("host", room)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute("host", "r1", "host"))
                .isInstanceOf(RestApiException.class);

        verify(roommateService, never()).leaveRoom(any(), any());
        verify(roomService, never()).flush();
        verify(eventPublisher, never()).publishEvent(any());
    }
}
