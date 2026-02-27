package com.project.dorumdorum.domain.chat.application.aop;

import com.project.dorumdorum.domain.chat.application.event.ChatEventPublisher;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestCreatedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestDecidedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatNotificationPublishAspect {

    private final ChatEventPublisher chatEventPublisher;
    private final ExpressionParser spelParser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @AfterReturning(pointcut = "@annotation(notificationPublish)", returning = "result")
    public void publish(JoinPoint joinPoint, NotificationPublish notificationPublish, Object result) {
            /*log.info("[FLOW][3_ASPECT_AFTER_RETURNING] thread={} transactionActive={} subject={} eventType={}",
            Thread.currentThread().getName(),
            TransactionSynchronizationManager.isActualTransactionActive(),
            notificationPublish.subject(),
            event == null ? "null" : event.getClass().getSimpleName());*/
        Object event = evaluateEvent(joinPoint, result, notificationPublish.event());

        if (event == null) {
            throw new IllegalStateException("Notification event is null for subject " + notificationPublish.subject());
        }

        switch (notificationPublish.subject()) {
            case MESSAGE_SENT -> {
                if (!(event instanceof MessageSentEvent messageSentEvent)) {
                    throw new IllegalStateException("Expected MessageSentEvent but got " + event.getClass().getName());
                }
                chatEventPublisher.publishMessageSent(messageSentEvent);
            }
            case MESSAGE_REQUEST_CREATED -> {
                if (!(event instanceof MessageRequestCreatedEvent createdEvent)) {
                    throw new IllegalStateException("Expected MessageRequestCreatedEvent but got " + event.getClass().getName());
                }
                chatEventPublisher.publishMessageRequestCreated(createdEvent);
            }
            case MESSAGE_REQUEST_DECIDED -> {
                if (!(event instanceof MessageRequestDecidedEvent decidedEvent)) {
                    throw new IllegalStateException("Expected MessageRequestDecidedEvent but got " + event.getClass().getName());
                }
                chatEventPublisher.publishMessageRequestDecided(decidedEvent);
            }
        }
    }

    private Object evaluateEvent(JoinPoint joinPoint, Object result, String expression) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        StandardEvaluationContext context = new MethodBasedEvaluationContext(
            joinPoint.getTarget(),
            method,
            joinPoint.getArgs(),
            parameterNameDiscoverer
        );
        context.setVariable("result", result);
        return spelParser.parseExpression(expression).getValue(context);
    }
}
