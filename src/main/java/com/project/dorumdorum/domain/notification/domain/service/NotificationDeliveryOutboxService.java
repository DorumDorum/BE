package com.project.dorumdorum.domain.notification.domain.service;

import com.github.f4b6a3.tsid.TsidCreator;
import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryOutboxStatus;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutboxStatus;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeliveryOutboxRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.NotificationErrorStatus.NOTIFICATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationDeliveryOutboxService {

    private static final int MAX_RETRY_COUNT = 5;
    private static final long BASE_BACKOFF_SECONDS = 5L;

    private final NotificationDeliveryOutboxRepository notificationDeliveryOutboxRepository;

    @Transactional
    public void saveInitByDevices(String notificationOutboxNo, List<Device> devices) {
        List<NotificationDeliveryOutbox> outboxes = devices.stream()
                .map(device -> NotificationDeliveryOutbox.createInit(
                        TsidCreator.getTsid().toString(),
                        notificationOutboxNo,
                        device.getId()
                ))
                .toList();
        notificationDeliveryOutboxRepository.saveAll(outboxes);
    }

    @Transactional(readOnly = true)
    public List<NotificationDeliveryOutbox> loadProcessableBatch(int size) {
        return notificationDeliveryOutboxRepository.findProcessableByStatus(
                NotificationDeliveryOutboxStatus.INIT,
                NotificationOutboxStatus.SUCCESS,
                LocalDateTime.now(),
                PageRequest.of(0, size)
        );
    }

    @Transactional
    public NotificationDeliveryOutbox success(String deliveryOutboxNo) {
        NotificationDeliveryOutbox outbox = findById(deliveryOutboxNo);
        outbox.success();
        return outbox;
    }

    @Transactional
    public NotificationDeliveryOutbox fail(String deliveryOutboxNo) {
        NotificationDeliveryOutbox outbox = findById(deliveryOutboxNo);
        outbox.failWithBackoff(MAX_RETRY_COUNT, BASE_BACKOFF_SECONDS);
        return outbox;
    }

    private NotificationDeliveryOutbox findById(String deliveryOutboxNo) {
        return notificationDeliveryOutboxRepository.findById(deliveryOutboxNo)
                .orElseThrow(() -> new RestApiException(NOTIFICATION_NOT_FOUND));
    }
}
