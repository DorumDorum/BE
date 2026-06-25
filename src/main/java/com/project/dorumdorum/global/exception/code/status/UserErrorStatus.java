package com.project.dorumdorum.global.exception.code.status;

import com.project.dorumdorum.global.exception.code.BaseCode;
import com.project.dorumdorum.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorStatus implements BaseCodeInterface {
    _PASSWORD_NOT_MATCHES(HttpStatus.BAD_REQUEST, "COMMON400", "비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "ROOM002", "이미 가입된 이메일입니다."),
    DUPLICATE_SIGN_UP_INFO(HttpStatus.BAD_REQUEST, "USER001", "이미 가입된 회원 정보입니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROOM002", "가입되지 않은 이메일입니다."),
    AGE_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON400", "나이 계산 파싱 오류입니다."),
    FAILED_SEND_VERIFY_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL001", "인증번호 전송에 실패하였습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "USER002", "이메일 인증이 완료되지 않았습니다."),
    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public BaseCode getCode() {
        return BaseCode.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
