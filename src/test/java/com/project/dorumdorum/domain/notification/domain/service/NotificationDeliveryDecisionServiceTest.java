package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import com.project.dorumdorum.domain.notification.domain.repository.UserPresenceRepository;
import com.project.dorumdorum.domain.notification.domain.service.strategy.DecisionRequest;
import com.project.dorumdorum.domain.notification.domain.service.strategy.NotificationDeliveryStrategy;
import com.project.dorumdorum.domain.notification.domain.service.strategy.NotificationDeliveryStrategyFactory;
import com.project.dorumdorum.domain.notification.infra.sse.SseConnectionChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationDeliveryDecisionService 단위 테스트")
class NotificationDeliveryDecisionServiceTest {

    @Mock
    private UserPresenceRepository userPresenceRepository;

    @Mock
    private NotificationDeliveryStrategyFactory strategyFactory;

    @Mock
    private SseConnectionChecker sseConnectionChecker;

    @Mock
    private NotificationDeliveryStrategy strategy;

    @InjectMocks
    private NotificationDeliveryDecisionService decisionService;

    @Test
    @DisplayName("decide는 Presence와 SSE 연결 여부를 포함한 DecisionRequest로 전략에 위임한다")
    void decide_DelegatesToStrategyWithPresenceAndSseFlag() {
        // given
        String recipientNo = "user-1";
        String deviceId = "device-1";
        NotificationType type = NotificationType.NEW_MESSAGE_RECEIVED;
        String relatedId = "room-1";

        UserPresence presence = UserPresence.inChatroom("room-2");

        when(userPresenceRepository.getPresence(recipientNo)).thenReturn(presence);
        when(strategyFactory.getStrategy(presence)).thenReturn(strategy);
        when(sseConnectionChecker.hasConnection(recipientNo, deviceId)).thenReturn(true);
        when(strategy.getChannel(any())).thenReturn(NotificationDeliveryChannel.SSE);

        // when
        NotificationDeliveryChannel result = decisionService.decide(recipientNo, deviceId, type, relatedId);

        // then
        assertThat(result).isEqualTo(NotificationDeliveryChannel.SSE);

        ArgumentCaptor<DecisionRequest> captor = ArgumentCaptor.forClass(DecisionRequest.class);
        verify(strategy).getChannel(captor.capture());

        DecisionRequest request = captor.getValue();
        assertThat(request.type()).isEqualTo(type);
        assertThat(request.relatedId()).isEqualTo(relatedId);
        assertThat(request.presence()).isEqualTo(presence);
        assertThat(request.hasSseConnection()).isTrue();
    }
}

