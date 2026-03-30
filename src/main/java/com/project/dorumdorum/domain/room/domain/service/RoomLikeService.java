package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomLike;
import com.project.dorumdorum.domain.room.domain.repository.RoomLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomLikeService {

    private final RoomLikeRepository roomLikeRepository;

    @Transactional
    public void like(String userNo, Room room) {
        if (roomLikeRepository.existsByUserNoAndRoom(userNo, room)) {
            return; // 이미 좋아요면 아무 것도 하지 않음 (멱등)
        }

        RoomLike entity = RoomLike.builder()
                .room(room)
                .userNo(userNo)
                .build();

        roomLikeRepository.save(entity);
    }

    @Transactional
    public void unlike(String userNo, Room room) {
        if (!roomLikeRepository.existsByUserNoAndRoom(userNo, room)) {
            return; // 좋아요가 아니면 아무 것도 하지 않음 (멱등)
        }

        roomLikeRepository.deleteByUserNoAndRoom(userNo, room);
    }
}
