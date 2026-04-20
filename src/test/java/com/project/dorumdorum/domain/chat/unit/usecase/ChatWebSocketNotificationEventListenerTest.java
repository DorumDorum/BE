package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.event.ChatWebSocketNotificationEvent;
import com.project.dorumdorum.domain.chat.application.event.ChatWebSocketNotificationEventListener;
import com.project.dorumdorum.domain.chat.infra.websocket.ChatWebSocketSendService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatWebSocketNotificationEventListener Unit Tests")
class ChatWebSocketNotificationEventListenerTest {

    @Mock private ChatWebSocketSendService chatWebSocketSendService;
    @InjectMocks private ChatWebSocketNotificationEventListener listener;

    @Test
    @DisplayName("개인 알림을 브로드캐스트보다 먼저 보낸다")
    void handle_SendsUserNotificationBeforeBroadcast() {
        Object userPayload = new Object();
        Object broadcastPayload = new Object();
        ChatWebSocketNotificationEvent event = new ChatWebSocketNotificationEvent(
                List.of(new ChatWebSocketNotificationEvent.BroadcastTask("chat-room-1", broadcastPayload)),
                List.of(new ChatWebSocketNotificationEvent.UserNotifyTask("user-1", userPayload))
        );

        listener.handle(event);

        InOrder inOrder = inOrder(chatWebSocketSendService);
        inOrder.verify(chatWebSocketSendService).notifyUser("user-1", userPayload);
        inOrder.verify(chatWebSocketSendService).broadcast("chat-room-1", broadcastPayload);
    }
}
