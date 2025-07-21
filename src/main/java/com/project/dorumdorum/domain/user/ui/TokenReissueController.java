package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.domain.service.TokenReissueService;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.annotation.RefreshToken;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenReissueController {

    private final TokenReissueService tokenReissueService;

    @PostMapping("/reissue")
    public BaseResponse<TokenReissueResponse> reissue(@CurrentUser Long userNo, @RefreshToken String refreshToken) {
        return BaseResponse.onSuccess(tokenReissueService.reissue(refreshToken, userNo));
    }
}
