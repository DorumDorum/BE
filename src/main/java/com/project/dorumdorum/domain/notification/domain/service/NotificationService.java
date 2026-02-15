package com.project.dorumdorum.domain.notification.domain.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.FIREBASE_TOKEN_NOT_FOUND;
import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.NOTIFICATION_FAILED;
import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final DomainEventLogger domainEventLogger;

    @Transactional
    public void saveToken(String userNo, String firebaseToken) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        user.updateFirebaseToken(firebaseToken);
    }

    @Transactional
    public String sendNotification(String receiverUserNo, String title, String body, Map<String, String> data, String imageUrl) {
        User receiver = userRepository.findById(receiverUserNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        if (!StringUtils.hasText(receiver.getFirebaseToken())) {
            throw new RestApiException(FIREBASE_TOKEN_NOT_FOUND);
        }

        Message.Builder messageBuilder = Message.builder()
                .setToken(receiver.getFirebaseToken())
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setImage(imageUrl)
                                .build()
                );

        if (!CollectionUtils.isEmpty(data)) {
            messageBuilder.putAllData(data);
        }

        try {
            // 알림 보내기
            String messageId = firebaseMessaging.send(messageBuilder.build());
            domainEventLogger.info("notification", "FCM_SENT", Map.of(
                    "messageId", messageId,
                    "userNo", receiverUserNo,
                    "tokenExists", true
            ));
            return messageId;
        } catch (FirebaseMessagingException e) {
            MessagingErrorCode errorCode = e.getMessagingErrorCode();

            // 만료/등록 해제된 토큰이면 토큰을 비우고 클라이언트가 재등록하도록 유도
            if (errorCode == MessagingErrorCode.UNREGISTERED || errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                domainEventLogger.warn("notification", "FCM_INVALID_OR_EXPIRED_TOKEN", Map.of(
                        "userNo", receiverUserNo,
                        "token", receiver.getFirebaseToken(),
                        "errorCode", String.valueOf(errorCode),
                        "message", String.valueOf(e.getMessage())
                ), e);
                receiver.updateFirebaseToken(null);
                throw new RestApiException(FIREBASE_TOKEN_NOT_FOUND);
            }

            domainEventLogger.error("notification", "FCM_SEND_FAILED", Map.of(
                    "userNo", receiverUserNo,
                    "token", receiver.getFirebaseToken(),
                    "errorCode", String.valueOf(errorCode),
                    "message", String.valueOf(e.getMessage())
            ), e);
            throw new RestApiException(NOTIFICATION_FAILED);
        }
    }
}

