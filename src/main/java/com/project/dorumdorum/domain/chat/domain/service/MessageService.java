package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import com.project.dorumdorum.domain.chat.domain.entity.Message;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.repository.MessageRepository;
import com.github.f4b6a3.tsid.TsidCreator;
import com.project.dorumdorum.domain.chat.infra.WebSocketChatEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final WebSocketChatEventPublisher eventPublisher;

    public Message saveMessage(Long roomId, Long senderId, String content) {
        Message message = Message.builder()
                .messageNo(TsidCreator.getTsid256().toLong())
                .messageRoomNo(roomId)
                .senderNo(senderId)
                .content(content)
                .messageType(MessageType.TEXT)
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }
}
