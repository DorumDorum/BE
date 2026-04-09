package com.project.dorumdorum.global.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordAlertEventListener {

    private final DiscordAlertSender discordAlertSender;

    @Async("discordExecutor")
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true
    )
    public void handle(SystemAlertEvent event) {
        try {
            discordAlertSender.send(event.alert());
        } catch (Exception e) {
            log.error("[Alert] Discord 전송 실패 (Async). title={}", event.alert().title(), e);
        }
    }
}
