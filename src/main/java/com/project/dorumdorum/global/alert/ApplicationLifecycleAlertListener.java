package com.project.dorumdorum.global.alert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationLifecycleAlertListener {

    private final SystemAlertPublisher systemAlertPublisher;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("[Lifecycle] Application ready. readinessPath=/actuator/health/readiness livenessPath=/actuator/health/liveness");
        systemAlertPublisher.publish(
                AlertSeverity.INFO,
                AlertType.DEPLOYMENT,
                "[배포] 서버 시작 완료",
                "dorumdorum 서버가 정상 시작되었습니다."
        );
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosed() {
        log.warn("[Lifecycle] Shutdown requested. graceful shutdown in progress");
        systemAlertPublisher.publish(
                AlertSeverity.WARN,
                AlertType.DEPLOYMENT,
                "[배포] 서버 종료",
                "dorumdorum 서버가 종료되고 있습니다."
        );
    }

    @EventListener
    public void onReadinessChanged(AvailabilityChangeEvent<ReadinessState> event) {
        log.info("[Availability] Readiness -> {}", event.getState());
    }

    @EventListener
    public void onLivenessChanged(AvailabilityChangeEvent<LivenessState> event) {
        log.info("[Availability] Liveness -> {}", event.getState());
    }
}
