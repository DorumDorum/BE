package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsPageResponse;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.pagination.CursorCodec;
import com.project.dorumdorum.global.pagination.CursorPage;
import com.project.dorumdorum.global.pagination.CursorQueryParams;
import com.project.dorumdorum.global.pagination.PaginationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindRoomsUseCase {

    private final UserService userService;
    private final RoomService roomService;
    private static final int LIMIT = 50;

    /**
     * 방 목록 커서 조회
     * - 필터와 커서 기준으로 방 목록을 조회
     * - 다음 페이지 커서를 포함한 결과를 반환
     */
    public FindRoomsPageResponse execute(String userNo, ChecklistFilterRequest request) {
        Gender gender = userService.findById(userNo).getGender();
        CursorQueryParams params = PaginationHelper.prepareCursorQuery(request.cursor(), LIMIT);

        List<FindRoomsResponse> responses = roomService.searchByCursor(
                gender,
                request,
                params.cursorCreatedAt(),
                params.cursorId(),
                params.cursorRemaining(),
                params.limitPlusOne()
        );

        CursorPage<FindRoomsResponse> page = PaginationHelper.buildCursorPage(
                responses,
                LIMIT,
                last -> request.sortType() == ChecklistFilterRequest.SortType.REMAINING
                        ? CursorCodec.encode(last.remaining(), last.createdAt(), last.roomNo())
                        : CursorCodec.encode(last.createdAt(), last.roomNo())
        );

        Long totalCount = request.cursor() == null
                ? roomService.countSearchResults(gender, request)
                : null;

        return FindRoomsPageResponse.of(page, totalCount);
    }
}
