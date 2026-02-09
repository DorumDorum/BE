package com.project.dorumdorum.domain.chat.presence;

import com.project.dorumdorum.global.security.UserIdPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class PresenceStompDisconnectListener {

    private final PresenceService presenceService;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        Principal principal = event.getUser();
        if (principal instanceof UserIdPrincipal userPrincipal) {
            presenceService.setAppActive(userPrincipal.getUserId());
        }
    }
}
