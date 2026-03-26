package com.project.dorumdorum.global.ratelimit;

import com.project.dorumdorum.global.properties.RateLimitProperties;
import com.project.dorumdorum.global.properties.RateLimitRuleProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RateLimitPolicyRegistry 단위 테스트")
class RateLimitPolicyRegistryTest {

    @Test
    @DisplayName("태그에 해당하는 정책을 반환한다")
    void get_ReturnsRateLimitRule() {
        RateLimitRuleProperties ruleProperties = new RateLimitRuleProperties();
        ruleProperties.setPermitsPerWindow(20L);
        ruleProperties.setWindowMillis(10_000L);
        ruleProperties.setTtlSeconds(20L);

        RateLimitProperties properties = new RateLimitProperties();
        properties.setRules(Map.of("fcm", ruleProperties));

        RateLimitPolicyRegistry registry = new RateLimitPolicyRegistry(properties);

        RateLimitRule rule = registry.get("fcm");

        assertThat(rule.permitsPerWindow()).isEqualTo(20L);
        assertThat(rule.windowMillis()).isEqualTo(10_000L);
        assertThat(rule.ttlSeconds()).isEqualTo(20L);
    }
}
