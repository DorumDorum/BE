package com.project.dorumdorum.global.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLoggingFilterTest {

    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    @DisplayName("traceId를 요청/응답/MDC에 일관되게 반영한다")
    void doFilterInternal_SetsAndPropagatesTraceId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        request.addHeader(RequestLoggingFilter.TRACE_ID_HEADER, "trace-from-header");
        MockHttpServletResponse response = new MockHttpServletResponse();

        requestLoggingFilter.doFilter(request, response, new MockFilterChain());

        assertThat(request.getAttribute(RequestLoggingFilter.TRACE_ID_KEY)).isEqualTo("trace-from-header");
        assertThat(response.getHeader(RequestLoggingFilter.TRACE_ID_HEADER)).isEqualTo("trace-from-header");
        assertThat(MDC.get(RequestLoggingFilter.TRACE_ID_KEY)).isNull();
    }
}
