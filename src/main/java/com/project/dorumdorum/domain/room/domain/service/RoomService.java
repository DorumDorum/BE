package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.ROOM_NOT_FOUND;
import static com.project.dorumdorum.global.exception.code.status.CommonErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room create(String userNo, Gender gender, RoomCreateRequest request) {
        Room entity = Room.builder()
                .capacity(request.capacity())
                .roomType(request.roomType())
                .residencePeriod(request.residencePeriod())
                .title(request.title())
                .hostUserNo(userNo)
                .gender(gender)
                .build();

        return roomRepository.save(entity);
    }

    public Room findById(String roomNo) {
        return roomRepository.findById(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public Room findByIdForUpdate(String roomNo) {
        return roomRepository.findByRoomNoForUpdate(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public List<FindRoomsResponse> searchByCursor(
            Gender gender,
            ChecklistFilterRequest request,
            LocalDateTime cursorCreatedAt,
            String cursorId,
            Integer cursorRemaining,
            int limitPlusOne
    ) {
        return roomRepository.findByCursor(gender, request, cursorCreatedAt, cursorId, cursorRemaining, limitPlusOne);
    }

    public FindRoomsResponse findMyRoom(String userNo) {
        return roomRepository.findMyRoom(userNo)
                .orElseThrow(() -> new RestApiException(ROOM_NOT_FOUND));
    }

    public List<FindRoomsResponse> findLikedRooms(String userNo) {
        return roomRepository.findLikedRooms(userNo);
    }

    public List<FindRoomsResponse> findAppliedRooms(String userNo) {
        return roomRepository.findAppliedRooms(userNo);
    }
}
