package com.project.dorumdorum.domain.chat.application.event;

public interface ChatEventPublisher {

    void publishMessageSent(MessageSentEvent event);
    void publishMessageRequestCreated(MessageRequestCreatedEvent event);
    void publishMessageRequestDecided(MessageRequestDecidedEvent event);
}
