package com.project.dorumdorum.global.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordAlertSender {

    private final DiscordAlertProperties properties;
    private final AlertDeduplicationService deduplicationService;
    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void send(SystemAlert alert) {
        try {
            if (deduplicationService.isDuplicate(alert)) {
                log.debug("[Alert] 중복 알림 건너뜀. title={}", alert.title());
                return;
            }
        } catch (IllegalStateException e) {
            log.warn("[Alert] Redis 비가용 상태로 중복 검사 건너뜀. title={}", alert.title());
            return;
        }

        String webhookUrl = properties.resolveWebhookUrl(alert.severity());
        if (webhookUrl == null || webhookUrl.isBlank()) {
            log.debug("[Alert] Discord webhook URL 미설정. title={}", alert.title());
            return;
        }

        Map<String, Object> payload = buildEmbedPayload(alert);

        try {
            String body = objectMapper.writeValueAsString(payload);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            log.warn("[Alert] Discord 전송 실패. title={}", alert.title(), e);
        }
    }

    private Map<String, Object> buildEmbedPayload(SystemAlert alert) {
        Map<String, Object> embed = new LinkedHashMap<>();
        embed.put("title", severityPrefix(alert.severity()) + " " + alert.title());
        embed.put("description", alert.message());
        embed.put("color", resolveColor(alert.severity()));
        embed.put("timestamp", Instant.now().toString());

        List<Map<String, Object>> fields = new ArrayList<>();
        fields.add(inlineField("심각도", alert.severity().name()));
        fields.add(inlineField("유형", alert.type().name()));

        if (alert.data() != null && !alert.data().isEmpty()) {
            fields.add(blockField("상세 정보", formatData(alert.data())));
        }

        embed.put("fields", fields);
        embed.put("footer", Map.of("text", "dorumdorum"));

        return Map.of("embeds", List.of(embed));
    }

    private String severityPrefix(AlertSeverity severity) {
        return switch (severity) {
            case CRITICAL -> "\uD83D\uDD34";
            case ERROR    -> "\uD83D\uDFE0";
            case WARN     -> "\uD83D\uDFE1";
            case INFO     -> "\uD83D\uDD35";
        };
    }

    private int resolveColor(AlertSeverity severity) {
        return switch (severity) {
            case CRITICAL -> 15158332; // #E74C3C 빨강
            case ERROR    -> 15105570; // #E67E22 주황
            case WARN     -> 16312092; // #F9CA24 노랑
            case INFO     -> 5793266;  // #5865F2 블루퍼플
        };
    }

    private Map<String, Object> inlineField(String name, String value) {
        return Map.of("name", name, "value", value, "inline", true);
    }

    private Map<String, Object> blockField(String name, String value) {
        return Map.of("name", name, "value", value, "inline", false);
    }

    private String formatData(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder("```\n");
        data.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
        sb.append("```");
        return sb.toString();
    }
}
