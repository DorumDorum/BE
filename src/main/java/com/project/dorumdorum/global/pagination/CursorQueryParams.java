package com.project.dorumdorum.global.pagination;

import java.time.LocalDateTime;

public record CursorQueryParams(
        LocalDateTime cursorCreatedAt,
        String cursorId,
        int limitPlusOne
) {}
