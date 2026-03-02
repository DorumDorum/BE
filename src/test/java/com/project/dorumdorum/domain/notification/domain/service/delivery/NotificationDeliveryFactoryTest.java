package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.infra.fcm.FcmNotificationDelivery;
import com.project.dorumdorum.domain.notification.infra.sse.SseNotificationDelivery;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@DisplayName("NotificationDeliveryFactory 단위 테스트")
class NotificationDeliveryFactoryTest {

    private final SseNotificationDelivery sse = mock(SseNotificationDelivery.class);
    private final FcmNotificationDelivery fcm = mock(FcmNotificationDelivery.class);

    private final NotificationDeliveryFactory factory = new NotificationDeliveryFactory(sse, fcm);

    @Test
    @DisplayName("SSE 채널이면 SseNotificationDelivery를 반환한다")
    void getDelivery_Sse() {
        NotificationDelivery delivery = factory.getDelivery(NotificationDeliveryChannel.SSE);
        assertThat(delivery).isSameAs(sse);
    }

    @Test
    @DisplayName("FCM 채널이면 FcmNotificationDelivery를 반환한다")
    void getDelivery_Fcm() {
        NotificationDelivery delivery = factory.getDelivery(NotificationDeliveryChannel.FCM);
        assertThat(delivery).isSameAs(fcm);
    }

    @Test
    @DisplayName("SKIP 채널에는 예외를 던진다")
    void getDelivery_UnsupportedChannel_Throws() {
        assertThatThrownBy(() -> factory.getDelivery(NotificationDeliveryChannel.SKIP))
                .isInstanceOf(RestApiException.class);
    }
}

