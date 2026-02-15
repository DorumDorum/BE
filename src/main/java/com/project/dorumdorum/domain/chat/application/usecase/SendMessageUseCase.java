package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.request.SendMessageSocketRequest;
import com.project.dorumdorum.domain.chat.application.event.ChatEventPublisher;
import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import com.project.dorumdorum.domain.chat.domain.entity.Message;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.MessageService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.CommonErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final MessageService messageService;
    private final MessageRoomService messageRoomService;
    private final ParticipantService participantService;
    private final UserService userService;
    private final ChatEventPublisher chatEventPublisher;

    @Transactional
    public MessageSentEvent execute(String senderId, String roomId, SendMessageSocketRequest request) {
        if (request == null || request.content() == null || request.content().isBlank()) {
            throw new RestApiException(CommonErrorStatus._BAD_REQUEST);
        }

        // 유저 검증
        User sender = userService.findById(senderId);
        MessageRoom messageRoom = messageRoomService.findById(roomId);
        if (messageRoom.getRoomStatus() != MessageRoomStatus.APPROVED) {
            throw new RestApiException(CommonErrorStatus._BAD_REQUEST);
        }
        participantService.findByUserNoAndMessageRoomNo(sender, roomId);

        String content = request.content().trim();
        Message message = messageService.saveMessage(roomId, senderId, content);
        messageRoomService.updateLastMessage(messageRoom, content, message.getSentAt());

        MessageSentEvent event = MessageSentEvent.create(message, sender.getName());  // senderName 추가
        chatEventPublisher.publishMessageSent(event);
        return event;
    }
}
