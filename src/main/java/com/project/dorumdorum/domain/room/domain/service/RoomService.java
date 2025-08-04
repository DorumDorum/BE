package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room create(Long userNo, RoomCreateRequest request) {
        Room entity = Room.builder()
                .capacity(request.capacity())
                .roomType(request.roomType())
                .tags(request.tags())
                .title(request.title())
                .build();

        return roomRepository.save(entity);
    }
}
