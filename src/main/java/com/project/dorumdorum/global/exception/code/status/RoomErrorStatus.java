package com.project.dorumdorum.global.exception.code.status;

import com.project.dorumdorum.global.exception.code.BaseCode;
import com.project.dorumdorum.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RoomErrorStatus implements BaseCodeInterface {
    USER_IN_ROOM(HttpStatus.BAD_REQUEST, "ROOM001", "이미 속한 방입니다."),
    DUPLICATE_JOIN_REQUEST(HttpStatus.BAD_REQUEST, "ROOM002", "이미 전송한 요청입니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM003", "존재하지 않는 방입니다."),
    COMPLETED_ROOM_EXISTS(HttpStatus.BAD_REQUEST, "ROOM004", "이미 확정된 방이 존재하는 유저입니다."),
    ALREADY_JOINED_USER(HttpStatus.BAD_REQUEST, "ROOM004", "이미 속한 방이 존재하는 유저입니다."),
    NO_PERMISSION_ON_ROOM(HttpStatus.UNAUTHORIZED, "ROOM005", "권한이 없습니다."),
    ROOM_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM006", "존재하지 않는 요청입니다."),
    ROOM_IS_NOT_FULL(HttpStatus.BAD_REQUEST, "ROOM007", "방이 가득차지 않았습니다."),
    ALREADY_CONFIRM_REQUEST(HttpStatus.BAD_REQUEST, "ROOM008", "이미 처리된 요청입니다."),
    ROOMMATE_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM009", "룸메이트를 찾을 수 없습니다."),
    ROOM_RULE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "ROOM010", "이미 방 규칙이 존재합니다."),
    CANNOT_APPLY_TO_OWN_ROOM(HttpStatus.BAD_REQUEST, "ROOM011", "자신이 만든 방에는 지원할 수 없습니다."),
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
