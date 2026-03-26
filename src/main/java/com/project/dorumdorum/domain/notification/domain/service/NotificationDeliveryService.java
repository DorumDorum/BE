package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
import com.project.dorumdorum.global.alert.AlertSeverity;
import com.project.dorumdorum.global.alert.AlertType;
import com.project.dorumdorum.global.alert.SystemAlertPublisher;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.NotificationErrorStatus.NOTIFICATION_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDeliveryService {

    private static final String RETRYABLE_TOKENS_CONTEXT_KEY = "retryableFcmTokens";

    private final NotificationDeviceService notificationDeviceService;
    private final NotificationDeliveryOrchestrator notificationDeliveryOrchestrator;
    private final SystemAlertPublisher systemAlertPublisher;

    @Retryable(
            retryFor = RestApiException.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 3000, multiplier = 2)
    )
    public void deliver(Notification notification) {
        RetryContext retryContext = RetrySynchronizationManager.getContext();
        NotificationDeliveryOrchestrator.DeliveryResult deliveryResult = notificationDeliveryOrchestrator.deliver(
                notification,
                resolveTargetDevices(notification, retryContext)
        );

        if (!deliveryResult.invalidTokens().isEmpty()) {
            notificationDeviceService.clearInvalidFcmTokens(deliveryResult.invalidTokens());
        }

        if (deliveryResult.hasRetryableFailure()) {
            if (retryContext != null) {
                retryContext.setAttribute(RETRYABLE_TOKENS_CONTEXT_KEY, deliveryResult.retryableTokens());
            }
            throw new RestApiException(NOTIFICATION_FAILED);
        }

        if (retryContext != null) {
            retryContext.removeAttribute(RETRYABLE_TOKENS_CONTEXT_KEY);
        }
    }

    @Recover
    public void recover(RestApiException e, Notification notification) {
        systemAlertPublisher.publish(
                AlertSeverity.ERROR,
                AlertType.EXTERNAL_API,
                "알림 재시도 최종 실패",
                "notificationNo=%s recipientNo=%s code=%s".formatted(
                        notification.getNotificationNo(),
                        notification.getRecipientNo(),
                        e.getErrorCode().getCode()
                )
        );

        log.warn("Notification delivery abandoned notificationNo={} recipientNo={}",
                notification.getNotificationNo(), notification.getRecipientNo(), e);
    }

    @SuppressWarnings("unchecked")
    private List<Device> resolveTargetDevices(
            Notification notification,
            RetryContext retryContext
    ) {
        if (retryContext == null) {
            return notificationDeviceService.findByUserNo(notification.getRecipientNo());
        }

        Object retryableTokens = retryContext.getAttribute(RETRYABLE_TOKENS_CONTEXT_KEY);
        if (retryableTokens instanceof List<?> tokens) {
            return notificationDeviceService.findByFcmTokens((List<String>) tokens);
        }

        return notificationDeviceService.findByUserNo(notification.getRecipientNo());
    }
}
