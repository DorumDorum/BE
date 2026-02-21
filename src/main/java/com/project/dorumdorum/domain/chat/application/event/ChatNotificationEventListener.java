package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.chat.notification.SseNotificationSender;
import com.project.dorumdorum.domain.notification.domain.NotificationChannel;
import com.project.dorumdorum.domain.presence.domain.service.PresenceService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatNotificationEventListener {

    private final ParticipantService participantService;
    private final PresenceService presenceService;
    private final SseNotificationSender sseNotificationSender;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageSent(MessageSentEvent event) {
        log.info("[NOTIFY] 메시지 전송 roomId={} senderId={}", event.roomId(), event.senderId());
        List<Participant> participants = participantService.findActiveParticipantsByRoomNo(event.roomId());
        boolean shouldSendStomp = false;

        for (Participant participant : participants) {
            String userId = participant.getUser().getUserNo();
            NotificationChannel channel = presenceService.decideMessageChannel(userId, event.roomId());
            log.info("[NOTIFY] userId={} channel={}", userId, channel);

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
                    log.warn("[SSE] 메시지 전송 실패. userId={} roomId={}", userId, event.roomId(), e);
                }
                if (delivered) {
                    continue;
                }
            }

            if (channel == NotificationChannel.FCM || channel == NotificationChannel.SSE) {
                try {
                log.info("[FCM] 전송 userId={}", userId);
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
                    log.warn("[FCM] 메시지 알림 전송 실패. userId={} roomId={}", userId, event.roomId(), e);
                }
            }
        }

        if (shouldSendStomp) {
            log.info("[STOMP] 브로드캐스트 roomId={}", event.roomId());
            messagingTemplate.convertAndSend("/sub/rooms/" + event.roomId(), event);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageRequestCreated(MessageRequestCreatedEvent event) {
        NotificationChannel channel = presenceService.decideRequestChannel(event.receiverId(), event.messageRoomId());

        if (channel == NotificationChannel.SSE) {
            boolean delivered = false;
            try {
                delivered = sseNotificationSender.sendRequestCreated(event.receiverId(), event);
            } catch (Exception e) {
                log.warn("[SSE] 채팅 요청 전송 실패. receiverId={} roomId={}",
                    event.receiverId(), event.messageRoomId(), e);
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
                        "roomId", String.valueOf(event.messageRoomId()),
                        "senderId", String.valueOf(event.senderId())
                    ),
                    null
                );
            } catch (Exception e) {
                log.warn("[FCM] 채팅 요청 알림 전송 실패. receiverId={} roomId={}",
                    event.receiverId(), event.messageRoomId(), e);
            }
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageRequestDecided(MessageRequestDecidedEvent event) {
        notifyDecision(event, event.senderId());
        notifyDecision(event, event.receiverId());
    }

    private void notifyDecision(MessageRequestDecidedEvent event, String userId) {
        NotificationChannel channel = presenceService.decideRequestChannel(userId, event.messageRoomId());

        if (channel == NotificationChannel.SSE) {
            boolean delivered = false;
            try {
                delivered = sseNotificationSender.sendRequestDecided(userId, event);
            } catch (Exception e) {
                log.warn("[SSE] 채팅 요청 결과 전송 실패. userId={} roomId={}", userId, event.messageRoomId(), e);
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
                        "roomId", String.valueOf(event.messageRoomId()),
                        "senderId", String.valueOf(event.senderId()),
                        "receiverId", String.valueOf(event.receiverId()),
                        "decision", event.decision().name()
                    ),
                    null
                );
            } catch (Exception e) {
                log.warn("[FCM] 채팅 요청 결과 알림 전송 실패. userId={} roomId={}",
                    userId, event.messageRoomId(), e);
            }
        }
    }
}
