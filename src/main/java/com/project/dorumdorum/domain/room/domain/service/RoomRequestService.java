package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomRequestService {

    private final RoomRequestRepository roomRequestRepository;

    public RoomRequest create(Long userNo, Room room, JoinRoomRequest request, Direction direction) {
        RoomRequest entity = RoomRequest.builder()
                .room(room)
                .userNo(userNo)
                .direction(direction)
                .introduction(request.introduction())
                .additionalMessage(request.additionalMessage())
                .build();

        return roomRequestRepository.save(entity);
    }

    public Boolean isDuplicateJoinRequest(Long userNo, Room room) {
        return roomRequestRepository.existsByUserNoAndRoom(userNo, room);
    }
}
