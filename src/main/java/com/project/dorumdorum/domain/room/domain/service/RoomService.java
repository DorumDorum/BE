package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.pagination.DecodedCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.ROOM_NOT_FOUND;
import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room create(Long userNo, RoomCreateRequest request) {
        Room entity = Room.builder()
                .capacity(request.capacity())
                .roomType(request.roomType())
                .title(request.title())
                .hostUserNo(userNo)
                .build();

        return roomRepository.save(entity);
    }

    public Room findById(Long roomNo) {
        return roomRepository.findById(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public List<FindRoomsResponse> findByCursor(Long userNo, RoomRelation relation, RoomType type, Integer capacity, RoomSort sort, DecodedCursor decodedCursor, int limitPlusOne) {
        return roomRepository.findByCursor(userNo, relation, type, capacity, sort, decodedCursor, limitPlusOne);
    }

    public FindRoomsResponse findMyRoom(Long userNo) {
        return roomRepository.findMyRoom(userNo)
                .orElseThrow(() -> new RestApiException(ROOM_NOT_FOUND));
    }
}
