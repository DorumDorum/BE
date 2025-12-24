package com.project.dorumdorum.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {

    private final ServiceAccount serviceAccount = new ServiceAccount();

    @Getter
    @Setter
    public static class ServiceAccount {
        private String path;
    }
}

