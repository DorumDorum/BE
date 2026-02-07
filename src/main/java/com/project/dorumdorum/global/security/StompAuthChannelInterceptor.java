package com.project.dorumdorum.global.security;

import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader(AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith(BEARER)) {
                throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
            }

            String token = authHeader.substring(BEARER.length());
            if (!tokenProvider.validateToken(token) || !tokenProvider.isAccessToken(token)) {
                throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
            }

            Long userId = tokenProvider.getId(token)
                    .orElseThrow(() -> new RestApiException(GlobalErrorStatus._UNAUTHORIZED));
            accessor.setUser(new UserIdPrincipal(userId));
        }

        return message;
    }
}
