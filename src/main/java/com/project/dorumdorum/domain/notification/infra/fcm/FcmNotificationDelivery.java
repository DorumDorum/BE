package com.project.dorumdorum.domain.notification.infra.fcm;

import com.google.firebase.messaging.*;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import com.project.dorumdorum.global.ratelimit.RateLimited;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmNotificationDelivery {

    private static final int MULTICAST_MAX_TOKENS = 500;
    private final FirebaseMessaging firebaseMessaging;

    @RateLimited(tag = "fcm", key = "#p0.recipientNo()")
    public MulticastSendResult sendMulticast(NotificationDeliveryPayload payload, List<String> rawTokens) {
        List<String> tokens = sanitizeTokens(rawTokens);
        if (tokens.isEmpty()) {
            return MulticastSendResult.empty();
        }

        List<String> retryableTokens = new ArrayList<>();
        List<String> invalidTokens = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i += MULTICAST_MAX_TOKENS) {
            int end = Math.min(i + MULTICAST_MAX_TOKENS, tokens.size());
            List<String> chunk = tokens.subList(i, end);

            MulticastMessage.Builder messageBuilder = MulticastMessage.builder();
            configureMessageCommon(messageBuilder, payload)
                    .addAllTokens(chunk);

            try {
                BatchResponse response = firebaseMessaging.sendEachForMulticast(messageBuilder.build());
                List<SendResponse> responses = response.getResponses();

                for (int index = 0; index < responses.size(); index++) {
                    SendResponse sendResponse = responses.get(index);
                    if (sendResponse.isSuccessful()) {
                        continue;
                    }

                    String token = chunk.get(index);
                    FirebaseMessagingException exception = sendResponse.getException();
                    if (isPermanentFailure(exception)) {
                        invalidTokens.add(token);
                    } else {
                        retryableTokens.add(token);
                    }
                }
            } catch (FirebaseMessagingException e) {
                retryableTokens.addAll(chunk);
                log.warn("[FCM] multicast failed userNo={} notificationNo={} chunkSize={} error={}",
                        payload.recipientNo(), payload.notificationNo(), chunk.size(), e.getMessage(), e);
            }
        }

        return new MulticastSendResult(retryableTokens, invalidTokens);
    }

    private MulticastMessage.Builder configureMessageCommon(MulticastMessage.Builder builder, NotificationDeliveryPayload payload) {
        String redirectPath = payload.redirectPath() != null ? payload.redirectPath() : "/";
        return builder
                .setNotification(Notification.builder()
                        .setTitle(payload.title())
                        .setBody(payload.body())
                        .build())
                .putData("notificationNo", payload.notificationNo())
                .putData("type", payload.type().name())
                .putData("relatedId", payload.relatedId() != null ? payload.relatedId() : "")
                .putData("redirectPath", redirectPath)
                .putData("clickAction", redirectPath)
                .setWebpushConfig(WebpushConfig.builder()
                        .setFcmOptions(WebpushFcmOptions.builder()
                                .setLink(redirectPath)
                                .build())
                        .build());
    }

    private static List<String> sanitizeTokens(List<String> rawTokens) {
        if (rawTokens == null || rawTokens.isEmpty()) {
            return List.of();
        }

        Set<String> unique = new LinkedHashSet<>();
        for (String token : rawTokens) {
            if (token != null && !token.isBlank()) {
                unique.add(token);
            }
        }
        return new ArrayList<>(unique);
    }

    private static boolean isPermanentFailure(FirebaseMessagingException exception) {
        if (exception == null) {
            return false;
        }
        MessagingErrorCode errorCode = exception.getMessagingErrorCode();
        return errorCode == MessagingErrorCode.UNREGISTERED || errorCode == MessagingErrorCode.INVALID_ARGUMENT;
    }

    public record MulticastSendResult(List<String> retryableTokens, List<String> invalidTokens) {
        public static MulticastSendResult empty() {
            return new MulticastSendResult(List.of(), List.of());
        }

        public boolean hasRetryableFailure() {
            return !retryableTokens.isEmpty();
        }
    }
}
