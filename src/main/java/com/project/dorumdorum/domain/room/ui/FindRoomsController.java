package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.FindRoomsUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.ui.spec.FindRoomsApiSpec;
import com.project.dorumdorum.global.common.BaseResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindRoomsController implements FindRoomsApiSpec {

    private final FindRoomsUseCase findRoomsUseCase;

    @Override
    public BaseResponse<CursorPage<FindRoomsResponse>> loadAll(
            @RequestParam RoomRelation relation,
            @RequestParam(required = false) List<RoomType> types,
            @RequestParam(required = false) List<Integer> capacities,
            @RequestParam(required = false) List<ResidencePeriod> residencePeriods,
            @RequestParam(required = false) RoomSort sort,
            @RequestParam(required = false) String cursor
    ) {
        return BaseResponse.onSuccess(findRoomsUseCase.execute(
                relation, types, capacities, residencePeriods, sort, cursor
        ));
    }
}
