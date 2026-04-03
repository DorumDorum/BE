package com.project.dorumdorum.global.pagination;

import java.util.List;
import java.util.function.Function;

public final class PaginationHelper {

    private PaginationHelper() {}

    public static int limitPlusOne(int limit) {
        return limit + 1;
    }

    public static CursorQueryParams prepareCursorQuery(String cursor, int limit) {
        int limitPlusOne = limitPlusOne(limit);
        if (cursor == null || cursor.isBlank()) {
            return new CursorQueryParams(null, null, null, limitPlusOne);
        }
        DecodedCursor decoded = CursorCodec.decode(cursor);
        return new CursorQueryParams(decoded.createdAt(), decoded.pk(), decoded.remaining(), limitPlusOne);
    }

    public static <T> CursorPage<T> buildCursorPage(List<T> results, int limit, Function<T, String> nextCursorEncoder) {
        String nextCursor = (results.size() > limit && !results.isEmpty())
                ? nextCursorEncoder.apply(results.get(limit - 1))
                : null;
        return toCursorPage(results, limit, nextCursor);
    }

    public static <T> CursorPage<T> toCursorPage(List<T> results, int limit, String nextCursor) {
        boolean hasNext = results.size() > limit;
        List<T> slice = hasNext ? results.subList(0, limit) : results;

        return new CursorPage<>(slice, nextCursor, hasNext);
    }
}