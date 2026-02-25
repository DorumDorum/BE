package com.project.dorumdorum.global.warmer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupWarmup implements ApplicationListener<ApplicationReadyEvent> {

    private final WarmupService warmupService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("[Warmup] Application ready, running startup warmup");
        warmupService.warm();
    }
}

