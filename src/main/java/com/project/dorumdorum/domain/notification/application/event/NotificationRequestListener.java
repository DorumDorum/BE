package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.repository.UserDeviceTokenRepository;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
import com.project.dorumdorum.domain.notification.domain.vo.Device;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRequestListener {

    private final NotificationService notificationService;
    private final UserDeviceTokenRepository userDeviceTokenRepository;
    private final NotificationDeliveryOrchestrator deliveryOrchestrator;

    @Async
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L, multiplier = 2.0)
    )
    public void handle(NotificationRequestEvent event) {
        Notification saved = notificationService.save(
                event.recipientNo(),
                event.title(),
                event.body(),
                event.type(),
                event.relatedId()
        );

        for (Device device : userDeviceTokenRepository.getDevices(saved.getRecipientNo())) {
            deliveryOrchestrator.deliver(saved, device);
        }
    }

    @Recover
    public void recover(Exception e, NotificationRequestEvent event) {
        log.warn("[NOTIFICATION] async delivery failed after retries. recipientNo={} type={} relatedId={}",
                event.recipientNo(), event.type(), event.relatedId(), e);
        // 향후: 실패 이벤트를 별도 큐/테이블에 적재하는 로직을 추가할 수 있음
    }
}
