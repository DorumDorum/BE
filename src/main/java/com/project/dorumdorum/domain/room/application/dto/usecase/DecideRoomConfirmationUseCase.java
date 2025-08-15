package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.Roommate;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DecideRoomConfirmationUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;

    public void approve(Long userNo, Long roomNo) {
        Room room = roomService.findById(roomNo);
        Roommate roommate = roommateService.findByUserNoAndRoom(userNo, room);

        roommate.approve();
        room.plusConfirmMate();
    }

    public void reject(Long userNo, Long roomNo) {
        Room room = roomService.findById(roomNo);

        room.updateStatus(RoomStatus.CONFIRM_PENDING);
        roommateService.findByRoom(room)
                .forEach(Roommate::cancelConfirm);

        room.minusCurrentMate();
        room.clearConfirmMate();


    }
}
