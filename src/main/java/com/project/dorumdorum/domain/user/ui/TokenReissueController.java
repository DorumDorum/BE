package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.application.usecase.TokenReissueUseCase;
import com.project.dorumdorum.domain.user.ui.spec.TokenReissueApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.annotation.RefreshToken;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenReissueController implements TokenReissueApiSpec {

    private final TokenReissueUseCase tokenReissueUseCase;

    @Override
    public BaseResponse<TokenReissueResponse> reissue(
            @CurrentUser String userNo,
            @RefreshToken String refreshToken
    ) {
        return BaseResponse.onSuccess(tokenReissueUseCase.execute(userNo, refreshToken));
    }
}
