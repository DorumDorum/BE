package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.application.event.RoomDeletedEvent;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.repository.RoomLikeRepository;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.CANNOT_DELETE_COMPLETED_ROOM;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.ROOM_HAS_MEMBERS;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteRoomUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;
    private final RoomRequestService roomRequestService;
    private final RoomRuleService roomRuleService;
    private final RoomLikeRepository roomLikeRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 방 삭제
     * - 방장 권한 검증
     * - 방장 외 룸메이트 존재 시 삭제 불가
     * - RoomRequest, RoomLike cascade 삭제 후 방 소프트 삭제
     * - 채팅방 삭제 이벤트 발행
     */
    public void execute(String requesterNo, String roomNo) {
        Room room = roomService.findByIdForUpdate(roomNo);

        if (!room.isHost(requesterNo)) {
            throw new RestApiException(NO_PERMISSION_ON_ROOM);
        }

        if (room.isCompleted()) {
            throw new RestApiException(CANNOT_DELETE_COMPLETED_ROOM);
        }

        if (room.getCurrentMateCount() > 1) {
            throw new RestApiException(ROOM_HAS_MEMBERS);
        }

        roommateService.leaveRoom(requesterNo, roomNo);
        roomRequestService.deleteAllByRoom(room);
        roomRuleService.deleteByRoomNo(roomNo);
        roomLikeRepository.deleteAllByRoom(room);
        room.delete();

        eventPublisher.publishEvent(new RoomDeletedEvent(roomNo));
    }
}
