package com.project.dorumdorum.global.alert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DiscordAlertProperties Unit Tests")
class DiscordAlertPropertiesTest {

    @Test
    @DisplayName("CRITICAL severity는 errorWebhookUrl 반환")
    void resolveWebhookUrl_Critical_ReturnsErrorUrl() {
        DiscordAlertProperties props = propsWithAll();
        assertThat(props.resolveWebhookUrl(AlertSeverity.CRITICAL)).isEqualTo("http://error");
    }

    @Test
    @DisplayName("ERROR severity는 errorWebhookUrl 반환")
    void resolveWebhookUrl_Error_ReturnsErrorUrl() {
        DiscordAlertProperties props = propsWithAll();
        assertThat(props.resolveWebhookUrl(AlertSeverity.ERROR)).isEqualTo("http://error");
    }

    @Test
    @DisplayName("WARN severity는 infoWebhookUrl 반환")
    void resolveWebhookUrl_Warn_ReturnsInfoUrl() {
        DiscordAlertProperties props = propsWithAll();
        assertThat(props.resolveWebhookUrl(AlertSeverity.WARN)).isEqualTo("http://info");
    }

    @Test
    @DisplayName("INFO severity는 infoWebhookUrl 반환")
    void resolveWebhookUrl_Info_ReturnsInfoUrl() {
        DiscordAlertProperties props = propsWithAll();
        assertThat(props.resolveWebhookUrl(AlertSeverity.INFO)).isEqualTo("http://info");
    }

    @Test
    @DisplayName("errorWebhookUrl 미설정 시 webhookUrl로 fallback")
    void resolveWebhookUrl_NoErrorUrl_FallbackToDefault() {
        DiscordAlertProperties props = new DiscordAlertProperties();
        props.setWebhookUrl("http://default");
        props.setInfoWebhookUrl("http://info");

        assertThat(props.resolveWebhookUrl(AlertSeverity.CRITICAL)).isEqualTo("http://default");
    }

    @Test
    @DisplayName("infoWebhookUrl 미설정 시 webhookUrl로 fallback")
    void resolveWebhookUrl_NoInfoUrl_FallbackToDefault() {
        DiscordAlertProperties props = new DiscordAlertProperties();
        props.setWebhookUrl("http://default");
        props.setErrorWebhookUrl("http://error");

        assertThat(props.resolveWebhookUrl(AlertSeverity.WARN)).isEqualTo("http://default");
    }

    @Test
    @DisplayName("모든 URL 미설정 시 null 반환")
    void resolveWebhookUrl_AllNull_ReturnsNull() {
        DiscordAlertProperties props = new DiscordAlertProperties();

        assertThat(props.resolveWebhookUrl(AlertSeverity.CRITICAL)).isNull();
    }

    private DiscordAlertProperties propsWithAll() {
        DiscordAlertProperties props = new DiscordAlertProperties();
        props.setWebhookUrl("http://default");
        props.setErrorWebhookUrl("http://error");
        props.setInfoWebhookUrl("http://info");
        return props;
    }
}
