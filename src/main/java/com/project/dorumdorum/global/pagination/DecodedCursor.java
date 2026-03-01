package com.project.dorumdorum.global.pagination;

import java.time.LocalDateTime;

public record DecodedCursor(
        LocalDateTime createdAt,
        String pk
) {}