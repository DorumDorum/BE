package com.project.dorumdorum.global.exception.code.status;

import com.project.dorumdorum.global.exception.code.BaseCode;
import com.project.dorumdorum.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationErrorStatus implements BaseCodeInterface {
    FIREBASE_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTIFICATION001", "수신자의 FCM 토큰이 등록되어 있지 않습니다."),
    NOTIFICATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION002", "알림 전송 중 서버 에러"),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION003", "알림을 찾을 수 없거나 권한이 없습니다."),
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
