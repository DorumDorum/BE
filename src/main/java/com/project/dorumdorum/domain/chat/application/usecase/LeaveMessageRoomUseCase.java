package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveMessageRoomUseCase {

    private final MessageRoomService messageRoomService;
    private final ParticipantService participantService;
    private final UserService userService;

    @Transactional
    public void execute(String userNo, String messageRoomNo) {
        // 유저 검증
        User user = userService.findById(userNo);

        // 채팅방 검증
        MessageRoom messageRoom = messageRoomService.findById(messageRoomNo);
        
        if (messageRoom.getRoomStatus() == MessageRoomStatus.DELETED) {
            throw new RestApiException(GlobalErrorStatus.MESSAGEROOM_NOT_FOUND);
        }

        // 참여자 검증
        Participant participant = participantService.findByUserNoAndMessageRoomNo(user, messageRoomNo);
        
        if (participant.getDeletedAt() != null) {
            throw new RestApiException(GlobalErrorStatus.MESSAGEROOM_ALREADY_LEFT);
        }

        // 퇴장 처리 (leftAt 설정)
        participant.softDelete();
    }
}
