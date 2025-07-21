package com.project.dorumdorum.global.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "include-email-domains")
public class IncludeEmailDomainProperties {

    private final List<String> domains;

    public boolean matches(String domain) {
        return domains.stream()
                .anyMatch(d -> d.equals(domain));
    }
}
