package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadMyRoommatesUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadMyRoommatesApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadMyRoommatesController implements LoadMyRoommatesApiSpec {

    private final LoadMyRoommatesUseCase loadMyRoommatesUseCase;

    @Override
    public BaseResponse<List<MyRoommateResponse>> load(
            @CurrentUser Long userNo
    ) {
        return BaseResponse.onSuccess(loadMyRoommatesUseCase.execute(userNo));
    }
}
