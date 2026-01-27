package com.project.dorumdorum.domain.chat.ui;


import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessageRoomResponse;
import com.project.dorumdorum.domain.chat.application.usecase.LoadMessageRoomListUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.LoadMessageRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMessageRoomsController implements LoadMessageRoomsApiSpec {

    // 채팅방 목록 줄 때 해당 요청을 보낸 유저가 채팅방마다 채팅 요청자인지 채팅 요청 수신자인지 알려주기

    private final LoadMessageRoomListUseCase loadMessageRoomListUseCase;

    @GetMapping("/api/message-rooms")
    public BaseResponse<CursorPage<LoadMessageRoomResponse>> loadMessageRoomList(
        @CurrentUser Long userNo,
        @RequestParam(required = false) String cursor
    ) {
        return BaseResponse.onSuccess(loadMessageRoomListUseCase.execute(userNo, cursor));
    }
}
