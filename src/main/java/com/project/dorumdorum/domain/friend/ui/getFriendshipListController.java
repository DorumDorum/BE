package com.project.dorumdorum.domain.friend.ui;

import com.project.dorumdorum.domain.friend.application.dto.response.GetFriendListResponse;
import com.project.dorumdorum.domain.friend.application.usecase.GetFriendshipUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class getFriendshipListController {
    private final GetFriendshipUseCase getFriendshipUseCase;

    @GetMapping("/api/friends")
    public BaseResponse<List<GetFriendListResponse>> getFriendList(@CurrentUser Long userNo) {
        return BaseResponse.onSuccess(getFriendshipUseCase.execute(userNo));
    }
}
