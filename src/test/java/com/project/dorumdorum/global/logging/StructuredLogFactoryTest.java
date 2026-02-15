package com.project.dorumdorum.global.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StructuredLogFactoryTest {

    private final StructuredLogFactory structuredLogFactory = new StructuredLogFactory();

    @Test
    @DisplayName("요청 완료 로그는 공통 필드와 응답 필드를 모두 포함한다")
    void requestCompleted_IncludesCommonAndResponseFields() {
        RequestLogContext context = new RequestLogContext("trace-1", "GET", "/api/test", "user-1", "127.0.0.1", 200);

        Map<String, Object> log = structuredLogFactory.requestCompleted(context, 45L, 120);

        assertThat(log)
                .containsEntry("category", "RESPONSE")
                .containsEntry("event", "REQUEST_COMPLETED")
                .containsEntry("traceId", "trace-1")
                .containsEntry("status", 200)
                .containsEntry("elapsedMs", 45L)
                .containsEntry("responseSize", 120);
    }

    @Test
    @DisplayName("도메인 이벤트 로그는 category와 payload를 포함한다")
    void domainEvent_IncludesCategoryAndPayload() {
        Map<String, Object> log = structuredLogFactory.domainEvent(
                "presence",
                "APP_ACTIVE",
                Map.of("userNo", "u1")
        );

        assertThat(log)
                .containsEntry("category", "DOMAIN_EVENT")
                .containsEntry("event", "presence")
                .containsEntry("action", "APP_ACTIVE")
                .containsEntry("userNo", "u1");
    }
}
