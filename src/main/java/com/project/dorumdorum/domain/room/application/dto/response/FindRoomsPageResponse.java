package com.project.dorumdorum.domain.room.application.dto.response;

import com.project.dorumdorum.global.pagination.CursorPage;

import java.util.List;

public record FindRoomsPageResponse(
        List<FindRoomsResponse> items,
        String nextCursor,
        boolean hasNext,
        Long totalCount
) {
    public static FindRoomsPageResponse of(CursorPage<FindRoomsResponse> page, Long totalCount) {
        return new FindRoomsPageResponse(page.items(), page.nextCursor(), page.hasNext(), totalCount);
    }
}
