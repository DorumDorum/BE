package com.project.dorumdorum.domain.presence.application.listener;

import com.project.dorumdorum.domain.presence.domain.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class PresenceStompConnectListener {

    private final PresenceService presenceService;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        Principal principal = event.getUser();
        if (principal instanceof Authentication authentication) {
            presenceService.onWsConnect(authentication.getName());
        }
    }
}
