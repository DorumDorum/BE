package com.project.dorumdorum.global.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WARN 이상 로그를 Discord webhook으로 전송하는 Logback Appender.
 *
 * <p>인메모리 TTL 기반 중복 제거를 통해 동일 로그의 반복 전송을 방지한다.
 * Redis 의존 없이 동작하며, AlertDeduplicationService(구조적 알림용)와 독립적으로 운영된다.
 *
 * <p>logback-spring.xml에서 반드시 AsyncAppender로 감싸서 사용해야 한다.
 * 직접 사용하면 HTTP 호출이 애플리케이션 스레드를 블로킹한다.
 *
 * <p>중복 TTL (AlertDeduplicationService와 동일하게 맞춤):
 * <ul>
 *   <li>ERROR: 300초 (5분)</li>
 *   <li>WARN:  900초 (15분)</li>
 * </ul>
 */
public class DiscordLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final long ERROR_DEDUP_MILLIS = 300_000L;
    private static final long WARN_DEDUP_MILLIS  = 900_000L;
    private static final int  MAX_MESSAGE_LEN    = 1_000;
    private static final int  MAX_STACKTRACE_LEN = 800;
    private static final int  CLEANUP_INTERVAL   = 200;

    /** logback-spring.xml의 <webhookUrl> 태그로 주입 */
    private String webhookUrl;

    private HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /**
     * key   = "{level}:{shortLoggerName}:{messageHash}"
     * value = expiry timestamp (millis)
     */
    private final ConcurrentHashMap<String, Long> dedupCache = new ConcurrentHashMap<>();
    private final AtomicInteger appendCount = new AtomicInteger(0);

    // -------------------------------------------------------------------------
    // 핵심 append 메서드
    // -------------------------------------------------------------------------

    @Override
    protected void append(ILoggingEvent event) {
        if (webhookUrl == null || webhookUrl.isBlank()) return;
        if (!event.getLevel().isGreaterOrEqual(Level.WARN)) return;

        periodicCleanup();

        if (isDuplicate(event)) {
            addInfo("[DiscordLogbackAppender] 중복 로그 건너뜀: " + buildDedupKey(event));
            return;
        }

        sendToDiscord(event);
    }

    // -------------------------------------------------------------------------
    // 중복 제거
    // -------------------------------------------------------------------------

    /**
     * TTL 내 동일 키가 존재하면 true(중복), 없거나 만료됐으면 false(신규).
     * ConcurrentHashMap.compute()로 원자적 처리.
     */
    private boolean isDuplicate(ILoggingEvent event) {
        String key  = buildDedupKey(event);
        long   now  = System.currentTimeMillis();
        long   ttl  = event.getLevel() == Level.ERROR ? ERROR_DEDUP_MILLIS : WARN_DEDUP_MILLIS;

        boolean[] isDup = {false};
        dedupCache.compute(key, (k, existingExpiry) -> {
            if (existingExpiry != null && existingExpiry > now) {
                isDup[0] = true;
                return existingExpiry;
            }
            return now + ttl;
        });
        return isDup[0];
    }

    private String buildDedupKey(ILoggingEvent event) {
        int msgHash = Math.abs(event.getFormattedMessage().hashCode());
        return event.getLevel().levelStr + ":" + shortLogger(event.getLoggerName()) + ":" + msgHash;
    }

    /** 200번마다 만료된 캐시 항목을 정리해 메모리 누수를 방지 */
    private void periodicCleanup() {
        if (appendCount.incrementAndGet() % CLEANUP_INTERVAL == 0) {
            long now = System.currentTimeMillis();
            dedupCache.entrySet().removeIf(e -> e.getValue() <= now);
        }
    }

    // -------------------------------------------------------------------------
    // Discord 전송
    // -------------------------------------------------------------------------

    private void sendToDiscord(ILoggingEvent event) {
        try {
            String payload = buildEmbedPayload(event);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            // addWarn()을 사용해 일반 로거를 우회 → 무한 루프 방지
            addWarn("[DiscordLogbackAppender] Discord 전송 실패: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Discord Embed 페이로드 구성
    // -------------------------------------------------------------------------

    private String buildEmbedPayload(ILoggingEvent event) {
        boolean isError = event.getLevel() == Level.ERROR;
        String  emoji   = isError ? "\uD83D\uDFE0" : "\uD83D\uDFE1";  // 🟠 / 🟡
        int     color   = isError ? 15105570 : 16312092;               // orange / yellow

        String title       = emoji + " " + event.getLevel().levelStr + " | " + shortLogger(event.getLoggerName());
        String description = buildDescription(event);

        return "{\"embeds\":[{"
                + "\"title\":\""       + escapeJson(title)       + "\","
                + "\"description\":\"" + escapeJson(description) + "\","
                + "\"color\":"         + color                   + ","
                + "\"timestamp\":\""   + Instant.ofEpochMilli(event.getTimeStamp()) + "\","
                + "\"footer\":{\"text\":\"dorumdorum\"}"
                + "}]}";
    }

    private String buildDescription(ILoggingEvent event) {
        String message = truncate(event.getFormattedMessage(), MAX_MESSAGE_LEN);
        StringBuilder sb = new StringBuilder("```\n").append(message).append("\n```");

        IThrowableProxy tp = event.getThrowableProxy();
        if (tp != null) {
            sb.append("\n```\n").append(extractStackTrace(tp)).append("\n```");
        }
        return sb.toString();
    }

    private String extractStackTrace(IThrowableProxy tp) {
        StringBuilder sb = new StringBuilder(tp.getClassName())
                .append(": ").append(tp.getMessage()).append("\n");

        for (StackTraceElementProxy step : tp.getStackTraceElementProxyArray()) {
            sb.append("\tat ").append(step.getSTEAsString()).append("\n");
            if (sb.length() >= MAX_STACKTRACE_LEN) {
                sb.append("... (truncated)");
                break;
            }
        }
        return sb.toString().trim();
    }

    // -------------------------------------------------------------------------
    // 유틸
    // -------------------------------------------------------------------------

    private String shortLogger(String loggerName) {
        if (loggerName == null) return "unknown";
        int idx = loggerName.lastIndexOf('.');
        return idx >= 0 ? loggerName.substring(idx + 1) : loggerName;
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen) + "..." : s;
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Logback XML 바인딩용 setter
    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    /** 테스트 전용 — 프로덕션 코드에서 호출 금지 */
    void setHttpClientForTest(HttpClient client) {
        this.httpClient = client;
    }
}
