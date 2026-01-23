package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.application.dto.request.SendMessageRequest;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.repository.MessageRoomRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;

    public MessageRoom create(
            SendMessageRequest request,
            MessageRoomType roomType,
            MessageRoomStatus roomStatus,
            String directRoomKey
    ) {
        MessageRoom entity = MessageRoom.builder()
                .roomType(roomType)
                .directRoomKey(directRoomKey)
                .activeDirectRoomKey(directRoomKey)
                .lastMessageAt(LocalDateTime.now())
                .lastMessage(request.initMessage())
                .roomStatus(roomStatus)
                .build();

        return messageRoomRepository.save(entity);
    }

    public boolean existsActiveDirectRoomByKey(String directRoomKey) {
        return messageRoomRepository.existsByActiveDirectRoomKeyAndRoomTypeAndRoomStatusIn(
            directRoomKey,
            MessageRoomType.DIRECT,
            EnumSet.of(MessageRoomStatus.REQUESTED, MessageRoomStatus.APPROVED)
        );
    }

    public MessageRoom findById(Long messageRoomNo) {
        return messageRoomRepository.findById(messageRoomNo)
            .orElseThrow(() -> new RestApiException(GlobalErrorStatus.MESSAGEROOM_NOT_FOUND));
    }

    public void updateStatusToApprove(MessageRoom messageRoom) {
        messageRoom.approve();
    }

    public void updateStatusToReject(MessageRoom messageRoom) {
        messageRoom.reject();
    }
}
