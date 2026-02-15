package com.project.dorumdorum.global.exception.code.status;

import com.project.dorumdorum.global.exception.code.BaseCode;
import com.project.dorumdorum.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatErrorStatus implements BaseCodeInterface {
    MESSAGE_SELF_REQUEST(HttpStatus.BAD_REQUEST, "MESSAGE001", "자기 자신에게 채팅 요청을 보낼 수 없습니다."),
    PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE002", "존재하지 않는 참여자입니다."),
    MESSAGEROOM_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MESSAGE003", "이미 채팅방이 존재합니다."),
    MESSAGEREQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE004", "존재하지 않는 채팅 요청입니다."),
    NOT_MESSAGE_REQUEST_RECEIVER(HttpStatus.UNAUTHORIZED, "MESSAGE005", "채팅 요청 수신자가 아닙니다. "),
    MESSAGEROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE006", "존재하지 않는 채팅방입니다."),
    MESSAGEREQUEST_ALREADY_DECIDED(HttpStatus.BAD_REQUEST, "MESSAGE007", "이미 처리된 채팅 요청입니다."),
    MESSAGEROOM_ALREADY_LEFT(HttpStatus.BAD_REQUEST, "MESSAGE008", "이미 퇴장한 채팅방입니다."),
    CANNOT_ADD_TO_DIRECT_ROOM(HttpStatus.BAD_REQUEST, "MESSAGE009", "1:1 채팅방에는 참여자를 추가할 수 없습니다."),
    PARTICIPANT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MESSAGE010", "이미 참여 중인 유저입니다."),
    CANNOT_DELETE_ROOM_WITH_PARTICIPANTS(HttpStatus.BAD_REQUEST, "MESSAGE011", "다른 참여자가 있어 채팅방을 삭제할 수 없습니다."),
    ONLY_HOST_CAN_DELETE_GROUP_ROOM(HttpStatus.FORBIDDEN, "MESSAGE012", "그룹 채팅방은 방장만 삭제할 수 있습니다."),
    ONLY_HOST_CAN_INVITE(HttpStatus.FORBIDDEN, "MESSAGE013", "그룹 채팅방에는 방장만 초대할 수 있습니다."),
    TARGET_USER_NOT_IN_ROOM(HttpStatus.BAD_REQUEST, "MESSAGE014", "초대할 유저가 방의 구성원이 아닙니다."),
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
