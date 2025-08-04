package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room create(RoomCreateRequest request) {
        Room entity = Room.builder()
                .capacity(request.capacity())
                .roomType(request.roomType())
                .tags(request.tags())
                .title(request.title())
                .build();

        return roomRepository.save(entity);
    }

    public Room findById(Long roomNo) {
        return roomRepository.findById(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}
