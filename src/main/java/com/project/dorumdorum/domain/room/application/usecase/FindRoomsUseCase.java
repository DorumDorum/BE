package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.global.pagination.CursorCodec;
import com.project.dorumdorum.global.pagination.CursorPage;
import com.project.dorumdorum.global.pagination.CursorQueryParams;
import com.project.dorumdorum.global.pagination.PaginationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindRoomsUseCase {

    private final RoomService roomService;
    private static final int LIMIT = 50;

    /**
     * 방 목록 커서 조회
     * - 필터와 커서 기준으로 방 목록을 조회
     * - 다음 페이지 커서를 포함한 결과를 반환
     */
    public CursorPage<FindRoomsResponse> execute(ChecklistFilterRequest request) {
        CursorQueryParams params = PaginationHelper.prepareCursorQuery(request.cursor(), LIMIT);

        List<FindRoomsResponse> responses = roomService.searchByCursor(
                request,
                params.cursorCreatedAt(),
                params.cursorId(),
                params.limitPlusOne()
        );

        return PaginationHelper.buildCursorPage(
                responses,
                LIMIT,
                last -> CursorCodec.encode(last.createdAt(), last.roomNo())
        );
    }
}
