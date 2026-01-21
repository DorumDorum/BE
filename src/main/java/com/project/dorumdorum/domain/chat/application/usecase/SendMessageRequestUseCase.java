package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.SendMessageRequest;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.MessageService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMessageRequestUseCase {

    private final MessageRoomService messageRoomService;
    private final MessageService messageService;
    private final ParticipantService participantService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Transactional
    public void execute(Long userNo, Long receiverNo, SendMessageRequest request) {
        // 유저 존재 확인
        User sender = userService.findById(userNo);
        User receiver = userService.findById(receiverNo);

        // 본인 요청 방지
        if(userNo.equals(receiverNo))
            throw new RestApiException(GlobalErrorStatus.MESSAGE_SELF_REQUEST);

        // 이미 채팅방이 존재하는지 확인
        if (participantService.existsDirectMessageRoomByUserIds(userNo, receiverNo)) {
            throw new RestApiException(GlobalErrorStatus.MESSAGEROOM_ALREADY_EXIST);
        }

        // 채팅방 생성
        String directRoomKey = buildDirectRoomKey(userNo, receiverNo);
        MessageRoom messageRoom;
        try {
            messageRoom = messageRoomService.create(
                    request,
                    MessageRoomType.DIRECT,
                    MessageRoomStatus.REQUESTED,
                    directRoomKey
            );
        } catch (DataIntegrityViolationException e) {
            throw new RestApiException(GlobalErrorStatus.MESSAGEROOM_ALREADY_EXIST);
        }

        // 참여자 생성
        participantService.create(sender, messageRoom.getMessageRoomNo());
        participantService.create(receiver, messageRoom.getMessageRoomNo());

        // 첫 메시지 있으면 저장
        if (request.initMessage() != null && !request.initMessage().isBlank()) {
            messageService.saveMessage(
                    messageRoom.getMessageRoomNo(),
                    userNo,
                    request.initMessage()
            );
        }

        // 채팅 요청 알림(Firebase 등) - 실패해도 흐름 유지
        notificationService.sendNotificationSafely(
                receiverNo,
                "새 채팅 요청",
                receiver.getNickname() + "님이 채팅 요청을 보냈습니다.",
                Map.of(
                        "roomId", String.valueOf(messageRoom.getMessageRoomNo()),
                        "senderId", String.valueOf(userNo)
                ),
                null
        );
    }

    private String buildDirectRoomKey(Long userNo, Long receiverNo) {
        long min = Math.min(userNo, receiverNo);
        long max = Math.max(userNo, receiverNo);
        return "DIRECT:" + min + ":" + max;
    }
}
