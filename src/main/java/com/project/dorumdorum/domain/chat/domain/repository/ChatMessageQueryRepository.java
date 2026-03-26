package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageQueryRepository {

    List<ChatMessage> findMessages(String chatRoomNo, LocalDateTime joinedAt,
                                   LocalDateTime cursorCreatedAt, String cursorMessageNo,
                                   int limitPlusOne);
}
