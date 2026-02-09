package com.project.dorumdorum.domain.chat.infra;

import com.project.dorumdorum.domain.chat.application.event.ChatEventPublisher;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestCreatedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestDecidedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class SpringChatEventPublisher implements ChatEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishMessageSent(MessageSentEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publishMessageRequestCreated(MessageRequestCreatedEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publishMessageRequestDecided(MessageRequestDecidedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
