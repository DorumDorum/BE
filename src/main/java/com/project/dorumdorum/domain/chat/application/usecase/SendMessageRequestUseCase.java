package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.request.SendMessageRequest;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestCreatedEvent;
import com.project.dorumdorum.domain.chat.application.aop.NotificationPublish;
import com.project.dorumdorum.domain.chat.application.aop.NotificationSubject;
import com.project.dorumdorum.domain.chat.domain.service.MessageRequestService;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.MessageService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMessageRequestUseCase {

    private final MessageRoomService messageRoomService;
    private final MessageService messageService;
    private final MessageRequestService messageRequestService;
    private final ParticipantService participantService;
    private final UserService userService;

    @Transactional
    @NotificationPublish(subject = NotificationSubject.MESSAGE_REQUEST_CREATED, event = "#result")
    public MessageRequestCreatedEvent execute(String userNo, String receiverNo, SendMessageRequest request) {
        // 유저 존재 확인
        User sender = userService.findById(userNo);
        User receiver = userService.findById(receiverNo);

        // 본인 요청 방지
        if(userNo.equals(receiverNo))
            throw new RestApiException(GlobalErrorStatus.MESSAGE_SELF_REQUEST);

        // 이미 채팅방이 존재하는지 확인
        String directRoomKey = buildDirectRoomKey(userNo, receiverNo);
        if (messageRoomService.existsActiveDirectRoomByKey(directRoomKey)) {
            throw new RestApiException(GlobalErrorStatus.MESSAGEROOM_ALREADY_EXIST);
        }

        // 채팅방 생성
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

        // 채팅 요청 생성
        messageRequestService.save(sender.getUserNo(), receiver.getUserNo(), messageRoom.getMessageRoomNo());

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

        return MessageRequestCreatedEvent.create(
            messageRoom.getMessageRoomNo(),
            userNo,
            receiverNo,
            sender.getNickname()
        );
    }

    private String buildDirectRoomKey(String userNo, String receiverNo) {
        String first = userNo;
        String second = receiverNo;
        if (first.compareTo(second) > 0) {
            first = receiverNo;
            second = userNo;
        }
        return "DIRECT:" + first + ":" + second;
    }
}
