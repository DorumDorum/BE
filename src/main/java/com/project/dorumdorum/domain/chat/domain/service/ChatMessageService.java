package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatRoom chatRoom, String senderNo, String content,
                            MessageType messageType, int unreadCount) {
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderNo(senderNo)
                .content(content)
                .messageType(messageType)
                .unreadCount(unreadCount)
                .build();
        return chatMessageRepository.save(message);
    }

    public int decreaseUnreadCount(String chatRoomNo, LocalDateTime fromTime) {
        return chatMessageRepository.decreaseUnreadCount(chatRoomNo, fromTime);
    }

    public List<ChatMessage> findMessages(String chatRoomNo, LocalDateTime joinedAt,
                                          LocalDateTime cursorCreatedAt, String cursorMessageNo,
                                          int limitPlusOne) {
        return chatMessageRepository.findMessages(chatRoomNo, joinedAt, cursorCreatedAt, cursorMessageNo, limitPlusOne);
    }

    public void deleteAllByChatRoom(String chatRoomNo) {
        chatMessageRepository.deleteByChatRoomNo(chatRoomNo);
    }
}
