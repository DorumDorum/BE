package com.project.dorumdorum.domain.room.domain.service;

import com.project.dorumdorum.domain.room.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRole;
import com.project.dorumdorum.domain.room.domain.entity.Roommate;
import com.project.dorumdorum.domain.room.domain.repository.RoommateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoommateService {

    private final RoommateRepository roommateRepository;

    public Roommate create(Long userNo, Room room, RoomRole roomRole) {
        Roommate entity = Roommate.builder()
                .userNo(userNo)
                .room(room)
                .confirmStatus(ConfirmStatus.PENDING)
                .roomRole(roomRole)
                .build();

        return roommateRepository.save(entity);
    }

    public Boolean isUserInRoom(Long userNo, Room room) {
        return roommateRepository.existsByUserNoAndRoom(userNo, room);
    }
}
