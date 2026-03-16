package com.project.dorumdorum.domain.notification.infra.fcm;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FcmNotificationDelivery 단위 테스트")
class FcmNotificationDeliveryTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @InjectMocks
    private FcmNotificationDelivery delivery;

    private NotificationDeliveryPayload payload(String recipientNo) {
        return new NotificationDeliveryPayload(
                "n1",
                recipientNo,
                "title",
                "body",
                NotificationType.NEW_MESSAGE_RECEIVED,
                "room-1",
                "/chat/room-1"
        );
    }

    @Test
    @DisplayName("유효 토큰이 있으면 멀티캐스트 전송을 수행한다")
    void sendMulticast_WithValidTokens_SendsMulticast() throws Exception {
        NotificationDeliveryPayload payload = payload("user-1");
        BatchResponse batchResponse = mock(BatchResponse.class);
        SendResponse sendResponse = mock(SendResponse.class);

        when(sendResponse.isSuccessful()).thenReturn(true);
        when(batchResponse.getResponses()).thenReturn(List.of(sendResponse));
        when(firebaseMessaging.sendEachForMulticast(any(MulticastMessage.class))).thenReturn(batchResponse);

        FcmNotificationDelivery.MulticastSendResult result = delivery.sendMulticast(payload, List.of("token-1"));

        ArgumentCaptor<MulticastMessage> captor = ArgumentCaptor.forClass(MulticastMessage.class);
        verify(firebaseMessaging).sendEachForMulticast(captor.capture());
        assertThat(captor.getValue()).isNotNull();
        assertThat(result.hasRetryableFailure()).isFalse();
        assertThat(result.invalidTokens()).isEmpty();
    }

    @Test
    @DisplayName("토큰이 비어있으면 FCM 호출 없이 빈 결과를 반환한다")
    void sendMulticast_WithoutToken_DoesNotSend() throws Exception {
        NotificationDeliveryPayload payload = payload("user-1");

        FcmNotificationDelivery.MulticastSendResult result = delivery.sendMulticast(payload, List.of("", " "));

        verify(firebaseMessaging, never()).sendEachForMulticast(any(MulticastMessage.class));
        assertThat(result.hasRetryableFailure()).isFalse();
        assertThat(result.invalidTokens()).isEmpty();
    }
}

