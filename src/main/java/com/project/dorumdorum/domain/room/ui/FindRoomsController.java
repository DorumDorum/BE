package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.FindRoomsUseCase;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.ui.spec.FindRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindRoomsController implements FindRoomsApiSpec {

    private final FindRoomsUseCase findRoomsUseCase;

    @Override
    public BaseResponse<CursorPage<FindRoomsResponse>> loadAll(
            @CurrentUser Long userNo,
            @RequestParam RoomRelation relation,
            @RequestParam(required = false) RoomType type,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) RoomSort sort,
            @RequestParam(required = false) String cursor
    ) {
        return BaseResponse.onSuccess(findRoomsUseCase.execute(
                userNo, relation, type, capacity, sort, cursor
        ));
    }
}
