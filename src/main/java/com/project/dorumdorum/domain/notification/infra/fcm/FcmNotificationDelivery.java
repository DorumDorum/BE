package com.project.dorumdorum.domain.notification.infra.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDelivery;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmNotificationDelivery implements NotificationDelivery {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    @Override
    public void send(NotificationDeliveryChannel channel, NotificationDeliveryPayload payload) {
        if (channel != NotificationDeliveryChannel.FCM)
            return;
        sendFcm(payload);
    }

    private void sendFcm(NotificationDeliveryPayload payload) {
        User user = userRepository.findById(payload.recipientNo()).orElse(null);
        if (user == null || user.getFirebaseToken() == null || user.getFirebaseToken().isBlank()) {
            log.debug("[FCM] skip: no token for userNo={}", payload.recipientNo());
            return;
        }

        Message message = Message.builder()
                .setToken(user.getFirebaseToken())
                .setNotification(Notification.builder()
                        .setTitle(payload.title())
                        .setBody(payload.body())
                        .build())
                .putData("notificationNo", payload.notificationNo())
                .putData("type", payload.type().name())
                .putData("relatedId", payload.relatedId() != null ? payload.relatedId() : "")
                .putData("redirectPath", payload.redirectPath() != null ? payload.redirectPath() : "")
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.warn("[FCM] send failed userNo={} notificationNo={}", payload.recipientNo(), payload.notificationNo(), e);
        }
    }
}
