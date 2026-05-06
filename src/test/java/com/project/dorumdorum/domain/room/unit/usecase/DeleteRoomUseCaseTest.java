package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.application.event.RoomDeletedEvent;
import com.project.dorumdorum.domain.room.application.usecase.DeleteRoomUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.repository.RoomLikeRepository;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.CANNOT_DELETE_COMPLETED_ROOM;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.ROOM_HAS_MEMBERS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteRoomUseCase Unit Tests")
class DeleteRoomUseCaseTest {

    @Mock private RoomService roomService;
    @Mock private RoommateService roommateService;
    @Mock private RoomRequestService roomRequestService;
    @Mock private RoomRuleService roomRuleService;
    @Mock private RoomLikeRepository roomLikeRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @InjectMocks private DeleteRoomUseCase useCase;

    @Test
    @DisplayName("방장이 아닌 사용자가 삭제 요청 시 NO_PERMISSION_ON_ROOM 예외")
    void execute_WhenNotHost_ThrowsNoPermission() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(room.isHost("member1")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("member1", "r1"))
                .isInstanceOf(RestApiException.class)
                .extracting(e -> ((RestApiException) e).getErrorCode().getCode())
                .isEqualTo(NO_PERMISSION_ON_ROOM.getCode().getCode());

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("COMPLETED 상태의 방은 삭제 불가 — CANNOT_DELETE_COMPLETED_ROOM 예외")
    void execute_WhenCompleted_ThrowsCannotDeleteCompletedRoom() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(room.isHost("host")).thenReturn(true);
        when(room.isCompleted()).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute("host", "r1"))
                .isInstanceOf(RestApiException.class)
                .extracting(e -> ((RestApiException) e).getErrorCode().getCode())
                .isEqualTo(CANNOT_DELETE_COMPLETED_ROOM.getCode().getCode());

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("현재 인원이 2명 이상이면 삭제 불가 — ROOM_HAS_MEMBERS 예외")
    void execute_WhenMembersExist_ThrowsRoomHasMembers() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(room.isHost("host")).thenReturn(true);
        when(room.isCompleted()).thenReturn(false);
        when(room.getCurrentMateCount()).thenReturn(2);

        assertThatThrownBy(() -> useCase.execute("host", "r1"))
                .isInstanceOf(RestApiException.class)
                .extracting(e -> ((RestApiException) e).getErrorCode().getCode())
                .isEqualTo(ROOM_HAS_MEMBERS.getCode().getCode());

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("방장 혼자일 때 삭제 성공 — cascade 삭제 및 RoomDeletedEvent 발행")
    void execute_WhenHostAlone_DeletesAndPublishesEvent() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(room.isHost("host")).thenReturn(true);
        when(room.isCompleted()).thenReturn(false);
        when(room.getCurrentMateCount()).thenReturn(1);

        useCase.execute("host", "r1");

        verify(room).delete();
        verify(roommateService).leaveRoom("host", "r1");
        verify(roomRequestService).deleteAllByRoom(room);
        verify(roomRuleService).deleteByRoomNo("r1");
        verify(roomLikeRepository).deleteAllByRoom(room);
        verify(eventPublisher).publishEvent(new RoomDeletedEvent("r1"));
    }

    @Test
    @DisplayName("cascade 삭제는 방 소프트 삭제 → 룸메이트 제거 → 요청/규칙/좋아요 삭제 순서로 수행")
    void execute_CascadeDeletesInOrder() {
        Room room = mock(Room.class);
        when(roomService.findByIdForUpdate("r1")).thenReturn(room);
        when(room.isHost("host")).thenReturn(true);
        when(room.isCompleted()).thenReturn(false);
        when(room.getCurrentMateCount()).thenReturn(1);

        useCase.execute("host", "r1");

        InOrder inOrder = inOrder(room, roommateService, roomRequestService, roomRuleService, roomLikeRepository, eventPublisher);
        inOrder.verify(room).delete();
        inOrder.verify(roommateService).leaveRoom("host", "r1");
        inOrder.verify(roomRequestService).deleteAllByRoom(room);
        inOrder.verify(roomRuleService).deleteByRoomNo("r1");
        inOrder.verify(roomLikeRepository).deleteAllByRoom(room);
        inOrder.verify(eventPublisher).publishEvent(new RoomDeletedEvent("r1"));
    }
}
