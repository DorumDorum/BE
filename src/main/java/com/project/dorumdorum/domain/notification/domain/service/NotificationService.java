package com.project.dorumdorum.domain.notification.domain.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class NotificationService {

    private final UserRepository userRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Transactional
    public void saveToken(Long userNo, String firebaseToken) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        user.updateFirebaseToken(firebaseToken);
    }

    @Transactional
    public String sendNotification(Long receiverUserNo, String title, String body, Map<String, String> data, String imageUrl) {
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
            log.info("[FCM] sent messageId={} to userNo={} tokenExists={}", messageId, receiverUserNo, true);
            return messageId;
        } catch (FirebaseMessagingException e) {
            MessagingErrorCode errorCode = e.getMessagingErrorCode();

            // 만료/등록 해제된 토큰이면 토큰을 비우고 클라이언트가 재등록하도록 유도
            if (errorCode == MessagingErrorCode.UNREGISTERED || errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                log.warn("[FCM] invalid/expired token. userNo={} token={} errorCode={} message={}",
                        receiverUserNo, receiver.getFirebaseToken(), errorCode, e.getMessage());
                receiver.updateFirebaseToken(null);
                throw new RestApiException(FIREBASE_TOKEN_NOT_FOUND);
            }

            log.error("[FCM] send failed. userNo={} token={} errorCode={} message={}",
                    receiverUserNo, receiver.getFirebaseToken(), errorCode, e.getMessage(), e);
            throw new RestApiException(NOTIFICATION_FAILED);
        }
    }
}

