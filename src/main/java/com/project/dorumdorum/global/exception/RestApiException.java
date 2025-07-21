package com.project.dorumdorum.global.exception;

import com.project.dorumdorum.global.exception.code.BaseCode;
import com.project.dorumdorum.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestApiException extends RuntimeException {

    private final BaseCodeInterface errorCode;

    public BaseCode getErrorCode() {
        return this.errorCode.getCode();
    }
}
