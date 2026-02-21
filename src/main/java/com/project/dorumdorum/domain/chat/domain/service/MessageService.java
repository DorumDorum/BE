package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.Message;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.repository.MessageRepository;
import com.github.f4b6a3.tsid.TsidCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message saveMessage(String roomId, String senderId, String content) {
        Message message = Message.builder()
                .messageNo(TsidCreator.getTsid256().toString())
                .messageRoomNo(roomId)
                .senderNo(senderId)
                .content(content)
                .messageType(MessageType.TEXT)
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    public List<Message> findMessagesByCursor(String messageRoomNo, String cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        
        if (cursor == null) {
            return messageRepository.findByMessageRoomNoOrderByMessageNoDesc(messageRoomNo, pageable);
        } else {
            return messageRepository.findByMessageRoomNoAndMessageNoLessThanOrderByMessageNoDesc(
                    messageRoomNo, cursor, pageable
            );
        }
    }

    public Optional<Message> findLatestMessage(String messageRoomNo) {
        List<Message> messages = messageRepository.findByMessageRoomNoOrderByMessageNoDesc(
            messageRoomNo,
            PageRequest.of(0, 1)
        );
        if (messages.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(messages.get(0));
    }
}
