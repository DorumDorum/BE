package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.application.dto.SendMessageRequest;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.repository.MessageRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .lastMessageAt(LocalDateTime.now())
                .lastMessage(request.initMessage())
                .roomStatus(roomStatus)
                .build();

        return messageRoomRepository.save(entity);
    }
}
