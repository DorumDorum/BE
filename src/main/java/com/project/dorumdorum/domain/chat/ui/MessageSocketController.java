package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.SendMessageSocketRequest;
import com.project.dorumdorum.domain.chat.application.usecase.SendMessageUseCase;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import com.project.dorumdorum.global.security.UserIdPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageSocketController {

    private final SendMessageUseCase sendMessageUseCase;

    @MessageMapping("/rooms/{roomId}")
    public void send(
            @DestinationVariable String roomId,
            @Valid SendMessageSocketRequest request,
            Principal principal
    ) {
        if (!(principal instanceof UserIdPrincipal userPrincipal)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }

        sendMessageUseCase.execute(userPrincipal.getUserId(), roomId, request);
    }
}
