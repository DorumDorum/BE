package com.project.dorumdorum.global.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class StompExceptionAdvice {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageExceptionHandler(RestApiException.class)
    public void handleRestApiException(RestApiException e, SimpMessageHeaderAccessor headerAccessor) {
        String userNo = (String) headerAccessor.getSessionAttributes().get("userNo");
        if (userNo == null) return;
        messagingTemplate.convertAndSendToUser(userNo, "/queue/errors",
                new StompErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage())
        );
    }
}
