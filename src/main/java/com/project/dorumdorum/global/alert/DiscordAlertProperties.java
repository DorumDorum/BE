package com.project.dorumdorum.global.alert;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "alert.discord")
public class DiscordAlertProperties {

    private String webhookUrl;
}

