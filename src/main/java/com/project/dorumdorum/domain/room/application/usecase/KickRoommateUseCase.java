package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.event.RoommateKickedEvent;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.CANNOT_KICK_SELF;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@Transactional
@RequiredArgsConstructor
public class KickRoommateUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;
    private final ApplicationEventPublisher eventPublisher;

    public void execute(String requesterNo, String roomNo, String kickedUserNo) {
        Room room = roomService.findById(roomNo);

        if (!roommateService.isHost(requesterNo, room)) {
            throw new RestApiException(NO_PERMISSION_ON_ROOM);
        }

        if (requesterNo.equals(kickedUserNo)) {
            throw new RestApiException(CANNOT_KICK_SELF);
        }

        roommateService.leaveRoom(kickedUserNo, roomNo);
        room.minusCurrentMate();

        eventPublisher.publishEvent(new RoommateKickedEvent(roomNo, kickedUserNo));
    }
}
