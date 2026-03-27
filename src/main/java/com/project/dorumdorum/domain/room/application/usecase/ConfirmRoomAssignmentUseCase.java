package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.event.RoomConfirmedEvent;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 방 배정 확정 처리
     * - 현재 사용자가 해당 방 룸메이트인지 검증
     * - 본인 확정 상태를 승인으로 변경
     * - 방이 가득 찼고 전원이 승인했으면 방 배정을 완료하고 이벤트를 발행
     */
    public void execute(String userNo, String roomNo) {
        Room room = roomService.findByIdForUpdate(roomNo);

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
                List<String> memberUserNos = allRoommates.stream()
                        .map(Roommate::getUserNo)
                        .toList();
                eventPublisher.publishEvent(new RoomConfirmedEvent(room.getRoomNo(), memberUserNos));
            }
        }
    }
}
