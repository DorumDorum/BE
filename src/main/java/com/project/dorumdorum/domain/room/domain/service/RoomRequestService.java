package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.ROOM_REQUEST_NOT_FOUND;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.DUPLICATE_JOIN_REQUEST;

@Service
@RequiredArgsConstructor
public class RoomRequestService {

    private final RoomRequestRepository roomRequestRepository;

    public RoomRequest create(String userNo, Room room, JoinRoomRequest request, Direction direction) {
        RoomRequest entity = RoomRequest.builder()
                .room(room)
                .userNo(userNo)
                .direction(direction)
                .introduction(request.introduction())
                .additionalMessage(request.additionalMessage())
                .build();

        try {
            return roomRequestRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new RestApiException(DUPLICATE_JOIN_REQUEST);
        }
    }

    public Boolean isDuplicateJoinRequest(String userNo, Room room) {
        return roomRequestRepository.existsByUserNoAndRoom(userNo, room);
    }

    public RoomRequest findById(String requestNo) {
        return roomRequestRepository.findById(requestNo)
                .orElseThrow(() -> new RestApiException(ROOM_REQUEST_NOT_FOUND));
    }

    public void delete(RoomRequest entity) {
        roomRequestRepository.delete(entity);
    }

    public List<RoomRequestApplicationResponse> findApplicationsByRoom(Room room) {
        return roomRequestRepository.findApplicationsByRoom(room);
    }

    public void cancelJoinRequest(String userNo, Room room) {
        RoomRequest request = roomRequestRepository
                .findByUserNoAndRoomAndDirection(userNo, room, Direction.USER_TO_ROOM)
                .orElseThrow(() -> new RestApiException(ROOM_REQUEST_NOT_FOUND));

        roomRequestRepository.delete(request);
    }
}
