package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OfflineDeliveryStrategy 단위 테스트")
class OfflineDeliveryStrategyTest {

    private final OfflineDeliveryStrategy strategy = new OfflineDeliveryStrategy();

    @Test
    @DisplayName("오프라인 상태에서는 항상 FCM 채널을 선택한다")
    void getChannel_AlwaysReturnsFcm() {
        DecisionRequest request = new DecisionRequest(
                NotificationType.ROOM_APPLICATION_APPROVED,
                null,
                UserPresence.offline(),
                false
        );

        NotificationDeliveryChannel channel = strategy.getChannel(request);

        assertThat(channel).isEqualTo(NotificationDeliveryChannel.FCM);
    }
}

