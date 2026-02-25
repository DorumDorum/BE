package com.project.dorumdorum.global.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordAlertSender {

    private final DiscordAlertProperties properties;
    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void send(SystemAlert alert) {
        String webhookUrl = properties.getWebhookUrl();
        if (webhookUrl == null || webhookUrl.isBlank()) {
            log.debug("Discord webhook URL is not configured. Skip alert. title={}", alert.title());
            return;
        }

        String content = buildContent(alert);
        Map<String, Object> payload = Map.of("content", content);

        try {
            String body = objectMapper.writeValueAsString(payload);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            log.warn("Failed to send Discord alert. title={}", alert.title(), e);
        }
    }

    private String buildContent(SystemAlert alert) {
        StringBuilder sb = new StringBuilder();

        sb.append("[")
                .append(alert.severity())
                .append("] ")
                .append(alert.title())
                .append("\n")
                .append(alert.message());

        if (alert.data() != null && !alert.data().isEmpty()) {
            sb.append("\n\n")
                    .append("details: ")
                    .append(alert.data());
        }

        return sb.toString();
    }
}

