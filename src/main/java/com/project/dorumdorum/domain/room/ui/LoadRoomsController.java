package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.LoadRoomsResponse;
import com.project.dorumdorum.domain.room.application.dto.usecase.LoadRoomsUseCase;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import com.project.dorumdorum.domain.room.ui.spec.LoadRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadRoomsController implements LoadRoomsApiSpec {

    private final LoadRoomsUseCase loadRoomsUseCase;

    @GetMapping("/api/rooms")
    public BaseResponse<CursorPage<LoadRoomsResponse>> loadAll(
            @CurrentUser Long userNo,
            @RequestParam RoomRelation relation,
            @RequestParam(required = false) List<Tag> tags,
            @RequestParam(required = false) RoomType type,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) RoomSort sort,
            @RequestParam(required = false) String cursor
    ) {
        return BaseResponse.onSuccess(loadRoomsUseCase.execute(
                userNo, relation, tags, type, capacity, sort, cursor
        ));
    }
}
