package com.project.dorumdorum.global.pagination;

import java.time.LocalDateTime;

public record CursorQueryParams(
        LocalDateTime cursorCreatedAt,
        String cursorId,
        Integer cursorRemaining,
        int limitPlusOne
) {}
