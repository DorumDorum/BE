package com.project.dorumdorum.domain.chat.infra.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatWebSocketSendService {

    private final SimpMessagingTemplate messagingTemplate;

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void broadcast(String chatRoomNo, Object payload) {
        messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoomNo, payload);
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void notifyUser(String userNo, Object payload) {
        messagingTemplate.convertAndSendToUser(userNo, "/queue/notification", payload);
    }

    // broadcast / notifyUser 모두 (String, Object) 시그니처이므로 단일 recover로 처리
    @Recover
    public void recoverSend(Exception e, String recipient, Object payload) {
        log.warn("[Chat] WebSocket 알림 최종 실패. recipient={}", recipient, e);
    }
}
