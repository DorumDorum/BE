package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.event.RoomApplicationSubmittedEvent;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyRoomUseCase {

    private final UserService userService;
    private final RoomRequestService roomRequestService;
    private final RoomService roomService;
    private final RoommateService roommateService;
    private final ApplicationEventPublisher eventPublisher;

    public String execute(String userNo, String roomNo, JoinRoomRequest request) {
        userService.validateExistsById(userNo);

        Room room = roomService.findById(roomNo);

        if (roommateService.isUserRoommate(userNo, roomNo)) {
            throw new RestApiException(CANNOT_APPLY_TO_OWN_ROOM);
        }
        if (roommateService.existsByUserNo(userNo))
            throw new RestApiException(ALREADY_JOINED_USER);
        if (roomRequestService.isDuplicateJoinRequest(userNo, room))
            throw new RestApiException(DUPLICATE_JOIN_REQUEST);

        String requestNo = roomRequestService.create(userNo, room, request, Direction.USER_TO_ROOM).getRoomRequestNo();

        // 지원자-방장 1:1 채팅방 생성 이벤트
        eventPublisher.publishEvent(new RoomApplicationSubmittedEvent(roomNo, userNo, room.getHostUserNo()));

        return requestNo;
    }
}
