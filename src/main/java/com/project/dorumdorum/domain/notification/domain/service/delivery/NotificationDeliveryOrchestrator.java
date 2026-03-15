package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.mapper.NotificationMapper;
import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.service.NotificationDeliveryDecisionService;
import com.project.dorumdorum.domain.notification.infra.fcm.FcmNotificationDelivery;
import com.project.dorumdorum.domain.notification.infra.sse.SseNotificationDelivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationDeliveryOrchestrator {

    private final NotificationDeliveryDecisionService decisionService;
    private final NotificationMapper notificationMapper;
    private final SseNotificationDelivery sseNotificationDelivery;
    private final FcmNotificationDelivery fcmNotificationDelivery;

    public DeliveryResult deliver(Notification notification, List<Device> devices) {
        NotificationDeliveryPayload payload = notificationMapper.toDeliveryPayload(notification);
        DeviceChannelBuckets buckets = classifyDevicesByChannel(notification, devices);

        deliverSse(payload, buckets.sseDevices());
        FcmNotificationDelivery.MulticastSendResult multicastResult = deliverFcmMulticast(payload, buckets.fcmTokens());

        return toDeliveryResult(multicastResult);
    }

    private DeviceChannelBuckets classifyDevicesByChannel(Notification notification, List<Device> devices) {
        List<Device> sseDevices = new ArrayList<>();
        List<String> fcmTokens = new ArrayList<>();

        for (Device device : devices) {
            NotificationDeliveryChannel channel = decisionService.decide(
                    notification.getRecipientNo(),
                    device.getDeviceId(),
                    notification.getType(),
                    notification.getRelatedId()
            );
            if (channel == NotificationDeliveryChannel.SSE) {
                sseDevices.add(device);
            } else if (channel == NotificationDeliveryChannel.FCM) {
                fcmTokens.add(device.getFcmToken());
            }
        }
        return new DeviceChannelBuckets(sseDevices, fcmTokens);
    }

    private void deliverSse(NotificationDeliveryPayload payload, List<Device> sseDevices) {
        for (Device device : sseDevices) {
            sseNotificationDelivery.send(payload, device);
        }
    }

    private FcmNotificationDelivery.MulticastSendResult deliverFcmMulticast(
            NotificationDeliveryPayload payload,
            List<String> fcmTokens
    ) {
        return fcmNotificationDelivery.sendMulticast(payload, fcmTokens);
    }

    private DeliveryResult toDeliveryResult(FcmNotificationDelivery.MulticastSendResult result) {
        return new DeliveryResult(result.hasRetryableFailure(), result.invalidTokens());
    }

    public record DeliveryResult(
            boolean hasRetryableFailure,
            List<String> invalidTokens
    ) {
    }

    private record DeviceChannelBuckets(
            List<Device> sseDevices,
            List<String> fcmTokens
    ) {
    }
}
