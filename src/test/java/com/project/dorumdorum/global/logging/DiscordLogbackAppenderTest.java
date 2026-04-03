package com.project.dorumdorum.global.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DiscordLogbackAppender 단위 테스트.
 * Spring Context 없이 순수 JUnit으로 실행하여 빠른 피드백을 제공한다.
 */
@DisplayName("DiscordLogbackAppender Unit Tests")
class DiscordLogbackAppenderTest {

    private DiscordLogbackAppender appender;
    private HttpClient mockHttpClient;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        appender = new DiscordLogbackAppender();
        appender.setWebhookUrl("https://discord.com/api/webhooks/test");

        // HttpClient mock 주입 (네트워크 호출 방지)
        mockHttpClient = mock(HttpClient.class);
        @SuppressWarnings("unchecked")
        HttpResponse<Object> mockResponse = mock(HttpResponse.class);
        when(mockHttpClient.send(any(HttpRequest.class), any())).thenReturn(mockResponse);
        appender.setHttpClientForTest(mockHttpClient);

        appender.start();
    }

    // =========================================================================
    // webhookUrl 미설정 시 skip
    // =========================================================================

    @ParameterizedTest(name = "webhookUrl={0}")
    @ValueSource(strings = {"", "   "})
    @DisplayName("webhookUrl이 빈 값이면 HTTP 전송을 건너뛴다")
    void append_WhenWebhookUrlBlank_SkipsHttpCall(String blankUrl) throws Exception {
        appender.setWebhookUrl(blankUrl);

        appender.append(mockWarnEvent("any message"));

        verify(mockHttpClient, never()).send(any(), any());
    }

    @Test
    @DisplayName("webhookUrl이 null이면 HTTP 전송을 건너뛴다")
    void append_WhenWebhookUrlNull_SkipsHttpCall() throws Exception {
        appender.setWebhookUrl(null);

        assertThatCode(() -> appender.append(mockWarnEvent("any message")))
                .doesNotThrowAnyException();
        verify(mockHttpClient, never()).send(any(), any());
    }

    // =========================================================================
    // 레벨 필터링
    // =========================================================================

    @Test
    @DisplayName("INFO 레벨 로그는 Discord로 전송하지 않는다")
    void append_WhenLevelInfo_SkipsHttpCall() throws Exception {
        ILoggingEvent event = mockEventWithLevel(Level.INFO, "info message");

        appender.append(event);

        verify(mockHttpClient, never()).send(any(), any());
    }

    @Test
    @DisplayName("DEBUG 레벨 로그는 Discord로 전송하지 않는다")
    void append_WhenLevelDebug_SkipsHttpCall() throws Exception {
        ILoggingEvent event = mockEventWithLevel(Level.DEBUG, "debug message");

        appender.append(event);

        verify(mockHttpClient, never()).send(any(), any());
    }

    @Test
    @DisplayName("WARN 레벨 로그는 Discord로 전송한다")
    void append_WhenLevelWarn_SendsToDiscord() throws Exception {
        ILoggingEvent event = mockWarnEvent("warn message");

        appender.append(event);

        verify(mockHttpClient, times(1)).send(any(), any());
    }

    @Test
    @DisplayName("ERROR 레벨 로그는 Discord로 전송한다")
    void append_WhenLevelError_SendsToDiscord() throws Exception {
        ILoggingEvent event = mockErrorEvent("error message");

        appender.append(event);

        verify(mockHttpClient, times(1)).send(any(), any());
    }

    // =========================================================================
    // 중복 제거 (dedup) — TTL 기반
    // =========================================================================

    @Test
    @DisplayName("동일 로그를 TTL 내에 두 번 append 하면 첫 번째만 전송된다")
    void append_WhenSameMessageWithinTtl_SendsOnlyOnce() throws Exception {
        ILoggingEvent event1 = mockWarnEvent("duplicate warn message");
        ILoggingEvent event2 = mockWarnEvent("duplicate warn message");

        appender.append(event1);
        appender.append(event2);

        // TTL 내 두 번째 호출은 중복으로 차단
        verify(mockHttpClient, times(1)).send(any(), any());
    }

    @Test
    @DisplayName("TTL 만료 후 동일 로그를 append 하면 다시 전송된다")
    void append_WhenSameMessageAfterTtlExpiry_SendsAgain() throws Exception {
        ILoggingEvent event = mockWarnEvent("expired warn message");

        // 만료된 엔트리를 dedupCache에 직접 주입
        ConcurrentHashMap<String, Long> dedupCache = getDedupCache();
        String key = buildExpectedKey(event);
        dedupCache.put(key, System.currentTimeMillis() - 1L); // 이미 만료된 엔트리

        appender.append(event);

        // 만료 후 → 새 전송
        verify(mockHttpClient, times(1)).send(any(), any());
    }

    @Test
    @DisplayName("다른 메시지는 TTL과 무관하게 각각 전송된다")
    void append_WhenDifferentMessages_BothSent() throws Exception {
        ILoggingEvent event1 = mockWarnEvent("first message");
        ILoggingEvent event2 = mockWarnEvent("second message");

        appender.append(event1);
        appender.append(event2);

        verify(mockHttpClient, times(2)).send(any(), any());
    }

    // =========================================================================
    // HTTP 전송 실패 — 예외 전파 없음
    // =========================================================================

    @Test
    @DisplayName("Discord HTTP 전송 실패 시 예외를 전파하지 않는다 (Logback 스레드 보호)")
    void append_WhenHttpFails_DoesNotPropagateException() throws Exception {
        when(mockHttpClient.send(any(), any())).thenThrow(new RuntimeException("Network error"));

        assertThatCode(() -> appender.append(mockErrorEvent("error message")))
                .doesNotThrowAnyException();
    }

    // =========================================================================
    // escapeJson 유틸 검증
    // =========================================================================

    @Test
    @DisplayName("로그 메시지에 특수문자가 포함되어도 예외 없이 전송된다")
    void append_WhenMessageHasSpecialChars_SendsWithoutException() throws Exception {
        // 개행, 따옴표, 백슬래시가 포함된 메시지
        ILoggingEvent event = mockErrorEvent("line1\nline2\t\"quoted\" \\ backslash");

        assertThatCode(() -> appender.append(event)).doesNotThrowAnyException();
        verify(mockHttpClient, times(1)).send(any(), any());
    }

    @Test
    @DisplayName("스택트레이스가 없는 로그도 정상 처리된다")
    void append_WhenNoThrowable_HandlesGracefully() throws Exception {
        ILoggingEvent event = mockWarnEvent("warn without exception");

        assertThatCode(() -> appender.append(event)).doesNotThrowAnyException();
        verify(mockHttpClient, times(1)).send(any(), any());
    }

    // =========================================================================
    // 헬퍼 메서드
    // =========================================================================

    private ILoggingEvent mockWarnEvent(String message) {
        return mockEventWithLevel(Level.WARN, message);
    }

    private ILoggingEvent mockErrorEvent(String message) {
        return mockEventWithLevel(Level.ERROR, message);
    }

    private ILoggingEvent mockEventWithLevel(Level level, String message) {
        ILoggingEvent event = mock(ILoggingEvent.class);
        when(event.getLevel()).thenReturn(level);
        when(event.getFormattedMessage()).thenReturn(message);
        when(event.getLoggerName()).thenReturn("com.example.TestClass");
        when(event.getTimeStamp()).thenReturn(System.currentTimeMillis());
        when(event.getThrowableProxy()).thenReturn(null);
        return event;
    }

    /** dedupCache 필드에 리플렉션으로 접근 */
    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<String, Long> getDedupCache() throws Exception {
        Field field = DiscordLogbackAppender.class.getDeclaredField("dedupCache");
        field.setAccessible(true);
        return (ConcurrentHashMap<String, Long>) field.get(appender);
    }

    /** buildDedupKey()와 동일한 로직으로 키 생성 (화이트박스 검증용) */
    private String buildExpectedKey(ILoggingEvent event) {
        String shortLogger = "TestClass"; // com.example.TestClass → TestClass
        int msgHash = Math.abs(event.getFormattedMessage().hashCode());
        return event.getLevel().levelStr + ":" + shortLogger + ":" + msgHash;
    }
}
