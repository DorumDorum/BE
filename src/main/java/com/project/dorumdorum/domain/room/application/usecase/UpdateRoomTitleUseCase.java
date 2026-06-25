package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomTitleRequest;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRoomTitleUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;

    /**
     * 방 제목 수정
     * - 방 정보를 조회하고 방장 권한을 검증
     * - 공백을 정리한 새 제목으로 변경
     */
    public void execute(String userNo, String roomNo, UpdateRoomTitleRequest request) {
        Room room = roomService.findById(roomNo);

        if (!roommateService.isHost(userNo, room))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        room.updateTitle(request.title().trim());
        room.updateNotes(request.notes());
    }
}
