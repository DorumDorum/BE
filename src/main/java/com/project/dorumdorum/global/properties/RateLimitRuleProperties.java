package com.project.dorumdorum.global.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateLimitRuleProperties {

    private long permitsPerWindow;
    private long windowMillis;
    private long ttlSeconds;
}
