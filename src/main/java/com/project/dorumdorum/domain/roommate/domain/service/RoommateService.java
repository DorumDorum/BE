package com.project.dorumdorum.domain.roommate.domain.service;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RoommateService {

    private final RoommateRepository roommateRepository;

    public Roommate create(String userNo, Room room, RoomRole roomRole) {
        Roommate entity = Roommate.builder()
                .userNo(userNo)
                .room(room)
                .confirmStatus(ConfirmStatus.PENDING)
                .roomRole(roomRole)
                .build();

        return roommateRepository.save(entity);
    }

    public Boolean isUserRoommate(String userNo, String roomNo) {
        return roommateRepository.existsByUserNoAndRoomNo(userNo, roomNo);
    }

    public Boolean existsByUserNo(String userNo) {
        return roommateRepository.existsByUserNo(userNo);
    }

    public Boolean isHost(String userNo, Room room) {
        Roommate roommate = findByUserNoAndRoom(userNo, room);
        return RoomRole.HOST.equals(roommate.getRoomRole());
    }

    public Roommate findByUserNoAndRoom(String userNo, Room room) {
        return roommateRepository.findByUserNoAndRoom(userNo, room)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public List<Roommate> findByRoom(Room room) {
        return roommateRepository.findByRoom(room);
    }

    public Optional<Roommate> findByUserNo(String userNo) {
        return roommateRepository.findByUserNo(userNo);
    }

    public List<MyRoommateResponse> findMyRoommates(String userNo) {
        return roommateRepository.findMyRoommates(userNo);
    }
}
