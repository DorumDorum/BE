package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.LoadRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.pagination.DecodedCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room create(RoomCreateRequest request, Long hostUserNo) {
        Room entity = Room.builder()
                .capacity(request.capacity())
                .roomType(request.roomType())
                .tags(request.tags())
                .title(request.title())
                .hostUserNo(hostUserNo)
                .build();

        return roomRepository.save(entity);
    }

    public Room findById(Long roomNo) {
        return roomRepository.findById(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public List<LoadRoomsResponse> findByCursor(Long userNo, RoomRelation relation, List<Tag> tags, RoomType type, Integer capacity, RoomSort sort, DecodedCursor decodedCursor, int limitPlusOne) {
        return roomRepository.findByCursor(userNo, relation, tags, type, capacity, sort, decodedCursor, limitPlusOne);
    }
}
