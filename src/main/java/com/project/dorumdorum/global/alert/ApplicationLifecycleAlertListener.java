package com.project.dorumdorum.global.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationLifecycleAlertListener {

    private final SystemAlertPublisher systemAlertPublisher;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        systemAlertPublisher.publish(
                AlertSeverity.INFO,
                AlertType.DEPLOYMENT,
                "[배포] 서버 시작 완료",
                "dorumdorum 서버가 정상 시작되었습니다."
        );
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosed() {
        systemAlertPublisher.publish(
                AlertSeverity.WARN,
                AlertType.DEPLOYMENT,
                "[배포] 서버 종료",
                "dorumdorum 서버가 종료되고 있습니다."
        );
    }
}
