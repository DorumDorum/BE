package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.request.SendChatMessageRequest;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.repository.ChatMessageRepository;
import com.project.dorumdorum.domain.notification.application.event.NotificationRequestPublisher;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendChatMessageUseCase {

    private final ChatMessageRepository chatMessageRepository;
    private final NotificationRequestPublisher notificationRequestPublisher;

    @Transactional
    public String execute(SendChatMessageRequest request) {
        ChatMessage message = ChatMessage.builder()
                .senderNo(request.senderNo())
                .recipientNo(request.recipientNo())
                .content(request.content())
                .build();

        ChatMessage saved = chatMessageRepository.save(message);

        String title = "새 메시지";
        String body = truncate(request.content(), 50);
        notificationRequestPublisher.publish(
                request.recipientNo(),
                title,
                body,
                NotificationType.NEW_MESSAGE_RECEIVED,
                saved.getMessageNo()
        );

        return saved.getMessageNo();
    }

    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen) + "...";
    }
}
