package com.project.dorumdorum.domain.presence.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.PresenceSignalRequest;
import com.project.dorumdorum.domain.presence.domain.service.PresenceService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import com.project.dorumdorum.global.security.UserIdPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PresenceSocketController {

    private final PresenceService presenceService;

    @MessageMapping("/presence/enter")
    public void enter(@Valid PresenceSignalRequest request, Principal principal) {
        if (!(principal instanceof UserIdPrincipal userPrincipal)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }
        presenceService.onMessageRoomEnter(
            userPrincipal.getUserId(),
            request.messageRoomNo(),
            request.lastReadMessageId(),
            request.lastReadSentAt()
        );
    }

    @MessageMapping("/presence/leave")
    public void leave(@Valid PresenceSignalRequest request, Principal principal) {
        if (!(principal instanceof UserIdPrincipal userPrincipal)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }
        presenceService.onMessageRoomLeave(
            userPrincipal.getUserId(),
            request.messageRoomNo(),
            request.lastReadMessageId(),
            request.lastReadSentAt()
        );
    }

    @MessageMapping("/presence/ping")
    public void ping(Principal principal) {
        if (!(principal instanceof UserIdPrincipal userPrincipal)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }
        presenceService.onWsActivity(userPrincipal.getUserId());
    }
}
