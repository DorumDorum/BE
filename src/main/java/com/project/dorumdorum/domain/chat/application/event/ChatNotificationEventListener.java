package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.chat.notification.SseNotificationSender;
import com.project.dorumdorum.domain.chat.presence.NotificationChannel;
import com.project.dorumdorum.domain.chat.presence.PresenceService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatNotificationEventListener {

    private final ParticipantService participantService;
    private final PresenceService presenceService;
    private final SseNotificationSender sseNotificationSender;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final DomainEventLogger domainEventLogger;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageSent(MessageSentEvent event) {
        domainEventLogger.info("chat_notification", "MESSAGE_SENT_EVENT", Map.of(
                "roomId", event.roomId(),
                "senderId", event.senderId()
        ));
        List<Participant> participants = participantService.findActiveParticipantsByRoomNo(event.roomId());
        boolean shouldSendStomp = false;

        for (Participant participant : participants) {
            String userId = participant.getUser().getUserNo();
            NotificationChannel channel = presenceService.decideMessageChannel(userId, event.roomId());
            domainEventLogger.info("chat_notification", "MESSAGE_CHANNEL_DECIDED", Map.of(
                    "userNo", userId,
                    "channel", channel.name()
            ));

            if (channel == NotificationChannel.STOMP) {
                shouldSendStomp = true;
                continue;
            }

            if (userId.equals(event.senderId())) {
                continue;
            }

            if (channel == NotificationChannel.SSE) {
                boolean delivered = false;
                try {
                    delivered = sseNotificationSender.sendMessage(userId, event);
                } catch (Exception e) {
                    domainEventLogger.warn("chat_notification", "SSE_MESSAGE_DELIVERY_FAILED", Map.of(
                            "userNo", userId,
                            "roomId", event.roomId()
                    ), e);
                }
                if (delivered) {
                    continue;
                }
            }

            if (channel == NotificationChannel.FCM || channel == NotificationChannel.SSE) {
                try {
                    domainEventLogger.info("chat_notification", "FCM_SEND_ATTEMPT", Map.of("userNo", userId));
                    notificationService.sendNotification(
                            userId,
                            "새 메시지",
                            event.content(),
                            Map.of(
                                    "roomId", String.valueOf(event.roomId()),
                                    "senderId", String.valueOf(event.senderId()),
                                    "messageId", String.valueOf(event.messageId())
                            ),
                            null
                    );
                } catch (Exception e) {
                    domainEventLogger.warn("chat_notification", "FCM_MESSAGE_DELIVERY_FAILED", Map.of(
                            "userNo", userId,
                            "roomId", event.roomId()
                    ), e);
                }
            }
        }

        if (shouldSendStomp) {
            domainEventLogger.info("chat_notification", "STOMP_BROADCAST", Map.of("roomId", event.roomId()));
            messagingTemplate.convertAndSend("/sub/rooms/" + event.roomId(), event);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageRequestCreated(MessageRequestCreatedEvent event) {
        NotificationChannel channel = presenceService.decideRequestChannel(event.receiverId(), event.roomId());

        if (channel == NotificationChannel.SSE) {
            boolean delivered = false;
            try {
                delivered = sseNotificationSender.sendRequestCreated(event.receiverId(), event);
            } catch (Exception e) {
                domainEventLogger.warn("chat_notification", "SSE_REQUEST_CREATED_DELIVERY_FAILED", Map.of(
                        "receiverId", event.receiverId(),
                        "roomId", event.roomId()
                ), e);
            }
            if (delivered) {
                return;
            }
        }

        if (channel == NotificationChannel.FCM || channel == NotificationChannel.SSE) {
            try {
                notificationService.sendNotification(
                    event.receiverId(),
                    "새 채팅 요청",
                    event.senderNickname() + "님이 채팅 요청이 도착했습니다.",
                    Map.of(
                        "roomId", String.valueOf(event.roomId()),
                        "senderId", String.valueOf(event.senderId())
                    ),
                    null
                );
            } catch (Exception e) {
                domainEventLogger.warn("chat_notification", "FCM_REQUEST_CREATED_DELIVERY_FAILED", Map.of(
                        "receiverId", event.receiverId(),
                        "roomId", event.roomId()
                ), e);
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageRequestDecided(MessageRequestDecidedEvent event) {
        notifyDecision(event, event.senderId());
        notifyDecision(event, event.receiverId());
    }

    private void notifyDecision(MessageRequestDecidedEvent event, String userId) {
        NotificationChannel channel = presenceService.decideRequestChannel(userId, event.roomId());

        if (channel == NotificationChannel.SSE) {
            boolean delivered = false;
            try {
                delivered = sseNotificationSender.sendRequestDecided(userId, event);
            } catch (Exception e) {
                domainEventLogger.warn("chat_notification", "SSE_REQUEST_DECIDED_DELIVERY_FAILED", Map.of(
                        "userNo", userId,
                        "roomId", event.roomId()
                ), e);
            }
            if (delivered) {
                return;
            }
        }

        if (channel == NotificationChannel.FCM || channel == NotificationChannel.SSE) {
            try {
                notificationService.sendNotification(
                    userId,
                    "채팅 요청 결과",
                    "요청이 " + event.decision().name() + "되었습니다.",
                    Map.of(
                        "roomId", String.valueOf(event.roomId()),
                        "senderId", String.valueOf(event.senderId()),
                        "receiverId", String.valueOf(event.receiverId()),
                        "decision", event.decision().name()
                    ),
                    null
                );
            } catch (Exception e) {
                domainEventLogger.warn("chat_notification", "FCM_REQUEST_DECIDED_DELIVERY_FAILED", Map.of(
                        "userNo", userId,
                        "roomId", event.roomId()
                ), e);
            }
        }
    }
}
