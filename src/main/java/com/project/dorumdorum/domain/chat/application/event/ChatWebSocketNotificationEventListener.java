package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.infra.websocket.ChatWebSocketSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ChatWebSocketNotificationEventListener {

    private final ChatWebSocketSendService chatWebSocketSendService;

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = false)
    public void handle(ChatWebSocketNotificationEvent event) {
        event.userNotifications().forEach(task ->
                chatWebSocketSendService.notifyUser(task.userNo(), task.payload()));
        event.broadcasts().forEach(task ->
                chatWebSocketSendService.broadcast(task.chatRoomNo(), task.payload()));
    }
}
