package com.project.dorumdorum.domain.notification.infra.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDelivery;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmNotificationDelivery implements NotificationDelivery {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void send(NotificationDeliveryChannel channel, NotificationDeliveryPayload payload,
                     Device device) {
        if (channel != NotificationDeliveryChannel.FCM)
            return;
        sendFcm(payload, device.getFcmToken());
    }

    private void sendFcm(NotificationDeliveryPayload payload, String fcmToken) {
        if (fcmToken == null || fcmToken.isBlank()) {
            log.debug("[FCM] skip: no token for userNo={}", payload.recipientNo());
            return;
        }

        String redirectPath = payload.redirectPath() != null ? payload.redirectPath() : "/";

        Message.Builder messageBuilder = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(payload.title())
                        .setBody(payload.body())
                        .build())
                .putData("notificationNo", payload.notificationNo())
                .putData("type", payload.type().name())
                .putData("relatedId", payload.relatedId() != null ? payload.relatedId() : "")
                .putData("redirectPath", redirectPath)
                .putData("clickAction", redirectPath);

        // 웹 푸시: 클릭 시 열릴 URL 설정
        messageBuilder.setWebpushConfig(WebpushConfig.builder()
                .setFcmOptions(WebpushFcmOptions.builder()
                        .setLink(redirectPath)
                        .build())
                .build());

        try {
            String messageId = firebaseMessaging.send(messageBuilder.build());
            log.info("[FCM] sent userNo={} notificationNo={} messageId={}", payload.recipientNo(), payload.notificationNo(), messageId);
        } catch (FirebaseMessagingException e) {
            log.warn("[FCM] send failed userNo={} notificationNo={} error={}", payload.recipientNo(), payload.notificationNo(), e.getMessage(), e);
        }
    }
}
