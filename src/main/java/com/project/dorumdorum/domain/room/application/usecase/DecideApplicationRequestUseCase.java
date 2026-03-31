package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.event.RoommateAcceptedEvent;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
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
public class DecideApplicationRequestUseCase {

    private final RoomRequestService roomRequestService;
    private final UserService userService;
    private final RoomService roomService;
    private final RoommateService roommateService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 지원 요청 승인
     * - 요청자와 지원자의 상태를 검증
     * - 방장 권한과 정원 초과 여부를 확인
     * - 룸메이트를 추가하고 승인 이벤트를 발행한 뒤 요청을 삭제
     */
    public void approve(String userNo, String roomNo, String roomRequestNo) {
        // 유저 존재 유무 검증
        userService.validateExistsById(userNo);

        // 원하는 방의 요청이 맞는지 확인
        RoomRequest roomRequest = roomRequestService.findById(roomRequestNo);
        if (roomRequest.getRoom().getRoomNo().equals(roomNo)) {
            throw new RestApiException(ROOM_REQUEST_NOT_FOUND);
        }

        // 지원자가 이미 속한 방이 있는지 검증
        if(roommateService.existsByUserNo(roomRequest.getUserNo()))
            throw new RestApiException(ALREADY_JOINED_USER);

        // 요청이 path roomNo에 속한 요청인지 검증한 뒤, 해당 방 기준으로 락과 권한 검증 수행
        Room room = roomService.findByIdForUpdate(roomNo);
        if(!roommateService.isHost(userNo, room))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        // 정원 초과 방지
        if (room.isFull()) {
            throw new RestApiException(ROOM_FULL);
        }

        // 방 인원수 +1
        room.plusCurrentMate();
        roommateService.create(roomRequest.getUserNo(), room, RoomRole.MEMBER);

        // 지원자에게 알림
        eventPublisher.publishEvent(
                new RoommateAcceptedEvent(room.getRoomNo(), roomRequest.getUserNo(), userNo)
        );

        // 모든 플로우를 거쳤다면 요청은 삭제
        roomRequestService.delete(roomRequest);
    }

    /**
     * 지원 요청 거절
     * - 요청자 존재 여부와 방장 권한을 검증
     * - 지원 요청을 삭제해 거절 처리
     */
    public void reject(String userNo, String roomRequestNo) {
        // 유저 존재 유무 검증
        userService.validateExistsById(userNo);

        RoomRequest roomRequest = roomRequestService.findById(roomRequestNo);
        if(!roommateService.isHost(userNo, roomRequest.getRoom()))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        // todo: 지원자에게 알림 roomRequest.getUserNo()
        roomRequestService.delete(roomRequest);
    }
}
