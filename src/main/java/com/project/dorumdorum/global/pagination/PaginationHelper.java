package com.project.dorumdorum.global.pagination;

import java.util.List;

public final class PaginationHelper {

    private PaginationHelper() {}

    public static int limitPlusOne(int limit) {
        return limit + 1;
    }

    public static <T> CursorPage<T> toCursorPage(List<T> results, int limit, String nextCursor) {
        boolean hasNext = results.size() > limit;
        List<T> slice = hasNext ? results.subList(0, limit) : results;

        return new CursorPage<>(slice, nextCursor, hasNext);
    }
}