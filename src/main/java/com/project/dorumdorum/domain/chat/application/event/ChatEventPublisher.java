package com.project.dorumdorum.domain.chat.application.event;

public interface ChatEventPublisher {

    void publishMessageSent(MessageSentEvent event);
}
