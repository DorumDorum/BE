package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.Roommate;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class InitiateRoomConfirmationUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;

    public void execute(Long userNo, Long roomNo) {
        Room room = roomService.findById(roomNo);

        if (!roommateService.isHost(userNo, room))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        if (!room.isFull())
            throw new RestApiException(ROOM_IS_NOT_FULL);

        if (!room.isPending())
            throw new RestApiException(ALREADY_CONFIRM_REQUEST);

        room.updateStatus(RoomStatus.IN_PROGRESS);
        List<Roommate> roommates = roommateService.findByRoom(room);
        // todo: 팀원 모두에게 알림
    }
}
