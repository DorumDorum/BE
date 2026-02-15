package com.project.dorumdorum.domain.room.unit.usecase;

import com.project.dorumdorum.domain.room.application.usecase.RoomLikeUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomLikeService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomLikeUseCase Unit Tests")
class RoomLikeUseCaseTest {

    @Mock private UserService userService;
    @Mock private RoomService roomService;
    @Mock private RoomLikeService roomLikeService;
    @InjectMocks private RoomLikeUseCase useCase;

    @Test
    @DisplayName("Should like room after validation")
    void like_ValidatesAndLikesRoom() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomService.findById("r1")).thenReturn(room);

        useCase.like("u1", "r1");

        verify(userService).validateExistsById("u1");
        verify(roomService).findById("r1");
        verify(roomLikeService).like("u1", room);
    }

    @Test
    @DisplayName("Should unlike room after validation")
    void unlike_ValidatesAndUnlikesRoom() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomService.findById("r1")).thenReturn(room);

        useCase.unlike("u1", "r1");

        verify(userService).validateExistsById("u1");
        verify(roomService).findById("r1");
        verify(roomLikeService).unlike("u1", room);
    }
}
