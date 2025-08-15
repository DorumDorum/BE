package com.project.dorumdorum.domain.room.application.dto.response;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;

import java.util.List;

public record LoadRoomsResponse (
        List<RoomSummary> items,     // 실제 방 데이터 목록
        String nextCursor,           // 다음 페이지 조회용 커서 (없으면 null)
        boolean hasNext,             // 다음 페이지 여부
        Meta meta                    // 메타데이터 (limit, 필터 정보, summary 등)
) {
    public record RoomSummary(
            Long roomNo,
            String title,
            String description,
            Integer remaining,       // 남은 인원
            List<Tag> additionalTag,
            String createdAt
    ) {}

    public record Meta(
            int limit,              // 서버가 응답하는 페이지 크기
            RoomRelation relation,  // RECRUITING / APPLIED / JOINED
            RoomSort sort,          // CREATED_AT / REMAINING
            RoomStatus status,      // CONFIRM_PENDING / IN_PROGRESS / COMPLETED
            RoomType type,          // 방 타입
            Integer capacity,       // 수용 인원
            List<Tag> tags          // 태그 목록
    ) {}
}