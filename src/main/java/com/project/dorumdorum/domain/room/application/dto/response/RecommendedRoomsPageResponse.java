package com.project.dorumdorum.domain.room.application.dto.response;

import java.util.List;

public record RecommendedRoomsPageResponse(
        boolean hasChecklist,
        List<RecommendedRoomResponse> items
) {}
