package com.project.dorumdorum.domain.chat.infra;

import com.project.dorumdorum.domain.chat.application.event.ChatEventPublisher;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestCreatedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestDecidedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class SpringChatEventPublisher implements ChatEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishMessageSent(MessageSentEvent event) {
        log.info("[FLOW][4_PUBLISH_EVENT] thread={} transactionActive={} eventType={}",
            Thread.currentThread().getName(),
            TransactionSynchronizationManager.isActualTransactionActive(),
            event.getClass().getSimpleName());
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
