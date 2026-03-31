package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadRoomApplicationsUseCase {

    private final RoomRequestService roomRequestService;
    private final RoomService roomService;
    private final RoommateService roommateService;

    /**
     * 방 지원자 목록 조회
     * - 방 정보를 조회하고 요청 사용자가 해당 방 멤버인지 검증
     * - 해당 방의 지원 요청 목록을 반환
     */
    public List<RoomRequestApplicationResponse> execute(String userNo, String roomNo) {
        Room room = roomService.findById(roomNo);

        if (!roommateService.isUserRoommate(userNo, roomNo)) {
            throw new RestApiException(NO_PERMISSION_ON_ROOM);
        }

        return roomRequestService.findApplicationsByRoom(room);
    }
}
