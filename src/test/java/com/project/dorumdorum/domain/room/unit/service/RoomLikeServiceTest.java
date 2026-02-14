package com.project.dorumdorum.domain.room.unit.service;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomLike;
import com.project.dorumdorum.domain.room.domain.repository.RoomLikeRepository;
import com.project.dorumdorum.domain.room.domain.service.RoomLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomLikeService Unit Tests")
class RoomLikeServiceTest {

    @Mock private RoomLikeRepository roomLikeRepository;
    @InjectMocks private RoomLikeService service;

    @Test
    @DisplayName("Should save like when not exists")
    void like_WhenNotExists_Saves() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomLikeRepository.existsByUserNoAndRoom("u1", room)).thenReturn(false);

        service.like("u1", room);

        verify(roomLikeRepository).save(any(RoomLike.class));
    }

    @Test
    @DisplayName("Should do nothing when like already exists")
    void like_WhenExists_DoesNothing() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomLikeRepository.existsByUserNoAndRoom("u1", room)).thenReturn(true);

        service.like("u1", room);

        verify(roomLikeRepository, never()).save(any(RoomLike.class));
    }

    @Test
    @DisplayName("Should delete like when exists")
    void unlike_WhenExists_Deletes() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomLikeRepository.existsByUserNoAndRoom("u1", room)).thenReturn(true);

        service.unlike("u1", room);

        verify(roomLikeRepository).deleteByUserNoAndRoom("u1", room);
    }

    @Test
    @DisplayName("Should do nothing when like does not exist")
    void unlike_WhenNotExists_DoesNothing() {
        Room room = Room.builder().roomNo("r1").build();
        when(roomLikeRepository.existsByUserNoAndRoom("u1", room)).thenReturn(false);

        service.unlike("u1", room);

        verify(roomLikeRepository, never()).deleteByUserNoAndRoom(anyString(), any(Room.class));
    }
}
