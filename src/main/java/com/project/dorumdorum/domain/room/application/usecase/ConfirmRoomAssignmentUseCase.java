package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmRoomAssignmentUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;

    public void execute(String userNo, String roomNo) {
        Room room = roomService.findById(roomNo);

        List<Roommate> allRoommates = roommateService.findByRoom(room);

        Roommate currentRoommate = allRoommates.stream()
                .filter(roommate -> roommate.getUserNo().equals(userNo))
                .findFirst()
                .orElseThrow(() -> new RestApiException(NO_PERMISSION_ON_ROOM));

        currentRoommate.accept();

        if(room.isFull()) {
            boolean allAccepted = allRoommates.stream()
                    .allMatch(roommate -> ConfirmStatus.ACCEPTED.equals(roommate.getConfirmStatus()));

            if (allAccepted) {
                allRoommates.forEach(Roommate::complete);
                room.updateStatus(RoomStatus.COMPLETED);
            }
        }
    }
}
