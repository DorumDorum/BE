package com.project.dorumdorum.global.pagination;

import java.time.LocalDateTime;

public record DecodedCursor(
        Integer remaining,
        LocalDateTime createdAt,
        Long pk
) {}