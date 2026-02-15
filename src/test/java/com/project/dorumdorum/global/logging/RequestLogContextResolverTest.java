package com.project.dorumdorum.global.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLogContextResolverTest {

    private final RequestLogContextResolver resolver = new RequestLogContextResolver();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("요청/응답과 인증 정보에서 컨텍스트를 추출한다")
    void resolve_WithRequestAndAuth_ExtractsContext() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/users/login");
        request.setAttribute(RequestLoggingFilter.TRACE_ID_KEY, "trace-123");
        request.addHeader("X-Forwarded-For", "1.1.1.1,2.2.2.2");

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(201);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user-99", null, List.of())
        );

        RequestLogContext context = resolver.resolve(request, response, 200);

        assertThat(context.traceId()).isEqualTo("trace-123");
        assertThat(context.method()).isEqualTo("POST");
        assertThat(context.uri()).isEqualTo("/api/users/login");
        assertThat(context.userNo()).isEqualTo("user-99");
        assertThat(context.ip()).isEqualTo("1.1.1.1");
        assertThat(context.status()).isEqualTo(201);
    }
}
