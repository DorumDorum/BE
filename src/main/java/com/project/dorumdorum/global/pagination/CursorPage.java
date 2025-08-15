package com.project.dorumdorum.global.pagination;

import java.util.List;

public record CursorPage<T>(
        List<T> items,
        String nextCursor,
        boolean hasNext
) {}