package com.project.dorumdorum.domain.friend.ui;

import com.project.dorumdorum.domain.friend.application.dto.response.LoadFriendshipResponse;
import com.project.dorumdorum.domain.friend.application.usecase.LoadFriendshipUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadFriendshipListController {
    private final LoadFriendshipUseCase loadFriendshipUseCase;

    @GetMapping("/api/friends")
    public BaseResponse<List<LoadFriendshipResponse>> LoadFriendList(@CurrentUser Long userNo) {
        return BaseResponse.onSuccess(loadFriendshipUseCase.execute(userNo));
    }
}
