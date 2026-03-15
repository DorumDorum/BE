package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(String senderNo, String recipientNo, String content) {
        ChatMessage message = ChatMessage.builder()
                .senderNo(senderNo)
                .recipientNo(recipientNo)
                .content(content)
                .build();

        return chatMessageRepository.save(message);
    }
}
