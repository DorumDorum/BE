package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddParticipantToMessageRoomUseCase {

    private final MessageRoomService messageRoomService;
    private final ParticipantService participantService;
    private final UserService userService;
    private final RoomService roomService;
    private final RoommateService roommateService;

    @Transactional
    public void execute(String userNo, String messageRoomNo, String targetUserNo) {
        // 유저 검증
        User targetUser = userService.findById(targetUserNo);

        // 본인 추가 방지
        if (userNo.equals(targetUserNo)) {
            throw new RestApiException(GlobalErrorStatus.MESSAGE_SELF_REQUEST);
        }

        // 채팅방 검증
        MessageRoom messageRoom = messageRoomService.findById(messageRoomNo);
        
        if (messageRoom.getRoomStatus() != MessageRoomStatus.APPROVED) {
            throw new RestApiException(GlobalErrorStatus._FORBIDDEN);
        }

        // DIRECT 채팅방에는 추가 불가
        if (messageRoom.getRoomType() == MessageRoomType.DIRECT) {
            throw new RestApiException(GlobalErrorStatus.CANNOT_ADD_TO_DIRECT_ROOM);
        }

        // 방장만 초대 가능 (GROUP 채팅방)
        if (messageRoom.getRoomNo() != null) {
            Room room = roomService.findById(messageRoom.getRoomNo());
            
            // 방장 확인
            if (!room.isHost(userNo)) {
                throw new RestApiException(GlobalErrorStatus.ONLY_HOST_CAN_INVITE);
            }
            
            // 초대할 유저가 Room의 구성원인지 확인
            if (!roommateService.isUserRoommate(targetUserNo, room.getRoomNo())) {
                throw new RestApiException(GlobalErrorStatus.TARGET_USER_NOT_IN_ROOM);
            }
        }

        // 대상 유저가 이미 참여 중인지 확인
        if (participantService.isParticipantInMessageRoom(targetUser, messageRoomNo)) {
            throw new RestApiException(GlobalErrorStatus.PARTICIPANT_ALREADY_EXISTS);
        }

        // 참여자 추가
        participantService.create(targetUser, messageRoomNo);
    }
}
