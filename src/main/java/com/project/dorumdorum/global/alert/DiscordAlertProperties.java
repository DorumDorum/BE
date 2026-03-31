package com.project.dorumdorum.global.alert;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "alert.discord")
public class DiscordAlertProperties {

    /** 공통 fallback webhook URL */
    private String webhookUrl;

    /** CRITICAL / ERROR 전용 채널 webhook URL */
    private String errorWebhookUrl;

    /** WARN / INFO 전용 채널 webhook URL */
    private String infoWebhookUrl;

    /**
     * severity에 맞는 webhook URL 반환.
     * error/info URL이 미설정이면 공통 webhookUrl로 fallback.
     */
    public String resolveWebhookUrl(AlertSeverity severity) {
        return switch (severity) {
            case CRITICAL, ERROR -> nonBlankOr(errorWebhookUrl, webhookUrl);
            case WARN, INFO      -> nonBlankOr(infoWebhookUrl, webhookUrl);
        };
    }

    private static String nonBlankOr(String preferred, String fallback) {
        return (preferred != null && !preferred.isBlank()) ? preferred : fallback;
    }
}
