package com.project.dorumdorum.global.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("JwtPrincipalHandshakeHandler Unit Tests")
class JwtPrincipalHandshakeHandlerTest {

    private final ExposedJwtPrincipalHandshakeHandler handshakeHandler = new ExposedJwtPrincipalHandshakeHandler();

    @Test
    @DisplayName("handshake attributes에 userNo가 있으면 StompPrincipal을 반환한다")
    void determineUser_WithUserNo_ReturnsStompPrincipal() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userNo", "user-1");

        Principal principal = handshakeHandler.determine(mock(ServerHttpRequest.class), mock(WebSocketHandler.class), attributes);

        assertThat(principal).isNotNull();
        assertThat(principal.getName()).isEqualTo("user-1");
    }

    private static final class ExposedJwtPrincipalHandshakeHandler extends JwtPrincipalHandshakeHandler {
        Principal determine(ServerHttpRequest request, WebSocketHandler handler, Map<String, Object> attributes) {
            return super.determineUser(request, handler, attributes);
        }
    }
}
