package com.project.dorumdorum.global.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenProvider tokenProvider;

    /**
     * HTTP handshake 시 accessToken 쿠키를 검증하고, 유효하면 userNo를 WebSocket 세션 속성에 저장
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) return false;

        Cookie[] cookies = servletRequest.getServletRequest().getCookies();
        if (cookies == null) return false;

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                String token = cookie.getValue();
                if (tokenProvider.validateToken(token) && tokenProvider.isAccessToken(token)) {
                    tokenProvider.getId(token).ifPresent(id -> attributes.put("userNo", id));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
