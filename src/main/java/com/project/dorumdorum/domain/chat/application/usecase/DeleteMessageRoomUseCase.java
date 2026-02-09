package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteMessageRoomUseCase {

    private final MessageRoomService messageRoomService;
    private final ParticipantService participantService;
    private final UserService userService;
    private final RoomService roomService;

    @Transactional
    public void execute(String userNo, String messageRoomNo) {
        // 유저 검증
        User user = userService.findById(userNo);

        // 채팅방 검증
        MessageRoom messageRoom = messageRoomService.findById(messageRoomNo);
        
        if (messageRoom.getRoomStatus() == MessageRoomStatus.DELETED) {
            throw new RestApiException(GlobalErrorStatus.MESSAGEROOM_NOT_FOUND);
        }

        // 요청자가 참여자인지 검증
        Participant requestParticipant = participantService.findByUserNoAndMessageRoomNo(user, messageRoomNo);
        
        if (requestParticipant.getDeletedAt() != null) {
            throw new RestApiException(GlobalErrorStatus.PARTICIPANT_NOT_FOUND);
        }

        // 모든 활성 참여자 조회
        List<Participant> activeParticipants = participantService.findActiveParticipantsByRoomNo(messageRoomNo);

        // 본인만 남았는지 확인 (본인 말고 다른 활성 참여자가 있으면 안됨)
        long otherActiveParticipantsCount = activeParticipants.stream()
                .filter(p -> !p.getUser().getUserNo().equals(userNo))
                .count();
        
        if (otherActiveParticipantsCount > 0) {
            throw new RestApiException(GlobalErrorStatus.CANNOT_DELETE_ROOM_WITH_PARTICIPANTS);
        }

        // GROUP 채팅방인 경우 방장만 삭제 가능
        if (messageRoom.getRoomType() == MessageRoomType.GROUP && messageRoom.getRoomNo() != null) {
            Room room = roomService.findById(messageRoom.getRoomNo());
            
            if (!room.isHost(userNo)) {
                throw new RestApiException(GlobalErrorStatus.ONLY_HOST_CAN_DELETE_GROUP_ROOM);
            }
        }

        // 본인 참여자 소프트 삭제
        requestParticipant.softDelete();

        // 채팅방 상태를 DELETED로 변경 및 deletedAt 업데이트
        messageRoomService.deleteMessageRoom(messageRoom);
    }
}
