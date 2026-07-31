package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.RoomDetailResponse;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.repository.RoomLikeRepository;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestRepository;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LoadRoomDetailUseCase {

    private final RoomService roomService;
    private final UserService userService;
    private final RoomLikeRepository roomLikeRepository;
    private final RoomRequestRepository roomRequestRepository;
    private final RoommateService roommateService;

    @Transactional(readOnly = true)
    public RoomDetailResponse execute(String userNo, String roomNo) {
        Room room = roomService.findById(roomNo);
        if (room.isDeleted()) {
            throw new RestApiException(ROOM_NOT_FOUND);
        }

        User host = userService.findById(room.getHostUserNo());
        boolean liked = roomLikeRepository.existsByUserNoAndRoom(userNo, room);
        boolean isMyRoom = room.isHost(userNo) || roommateService.isUserRoommate(userNo, roomNo);
        RoomDetailResponse.AppliedStatus appliedStatus = resolveAppliedStatus(userNo, room, isMyRoom);

        return new RoomDetailResponse(
                room.getRoomNo(),
                room.getRoomType(),
                room.getCapacity(),
                room.getCurrentMateCount(),
                room.getRemaining(),
                room.getTitle(),
                room.getNotes(),
                room.getHostUserNo(),
                host.getName(),
                host.getNickname(),
                host.getMajor(),
                host.getStudentNo().substring(2, 4),
                room.getResidencePeriod().name(),
                room.getRoomStatus(),
                liked,
                appliedStatus,
                isMyRoom
        );
    }

    private RoomDetailResponse.AppliedStatus resolveAppliedStatus(String userNo, Room room, boolean isMyRoom) {
        if (isMyRoom) {
            return RoomDetailResponse.AppliedStatus.APPROVED;
        }
        return roomRequestRepository.findByUserNoAndRoomAndDirection(userNo, room, Direction.USER_TO_ROOM).isPresent()
                ? RoomDetailResponse.AppliedStatus.WAITING
                : RoomDetailResponse.AppliedStatus.NONE;
    }
}
