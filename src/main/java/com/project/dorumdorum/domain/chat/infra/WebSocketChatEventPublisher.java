package com.project.dorumdorum.domain.chat.infra;

import com.project.dorumdorum.domain.chat.application.event.ChatEventPublisher;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestCreatedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestDecidedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketChatEventPublisher implements ChatEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publishMessageSent(MessageSentEvent event) {
        messagingTemplate.convertAndSend("/sub/rooms/" + event.roomId(), event);
    }

    @Override
    public void publishMessageRequestCreated(MessageRequestCreatedEvent event) {
        //
    }

    @Override
    public void publishMessageRequestDecided(MessageRequestDecidedEvent event) {
        //
    }
}
