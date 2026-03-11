package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeviceRepository;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationRequestListener 단위 테스트")
class NotificationRequestListenerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationDeviceRepository notificationDeviceRepository;

    @Mock
    private NotificationDeliveryOrchestrator deliveryOrchestrator;

    @InjectMocks
    private NotificationRequestListener listener;

    @Test
    @DisplayName("handle은 알림을 저장하고 각 디바이스로 전달을 위임한다")
    void handle_SavesAndDeliversToEachDevice() {
        // given
        NotificationRequestEvent event = new NotificationRequestEvent(
                "user-1", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "room-1"
        );

        Notification saved = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();

        List<Device> devices = List.of(
                Device.builder().id("id1").userNo("user-1").deviceId("d1").fcmToken("t1").build(),
                Device.builder().id("id2").userNo("user-1").deviceId("d2").fcmToken("t2").build()
        );

        when(notificationService.save(event.recipientNo(), event.title(), event.body(), event.type(), event.relatedId()))
                .thenReturn(saved);
        when(notificationDeviceRepository.findByUserNo("user-1")).thenReturn(devices);

        // when
        listener.handle(event);

        // then
        verify(notificationService).save(event.recipientNo(), event.title(), event.body(), event.type(), event.relatedId());
        verify(notificationDeviceRepository).findByUserNo("user-1");
        verify(deliveryOrchestrator).deliver(saved, devices.get(0));
        verify(deliveryOrchestrator).deliver(saved, devices.get(1));
    }
}

