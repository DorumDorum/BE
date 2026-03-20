package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.application.usecase.LoadMyChatRoomsUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.LoadMyChatRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadMyChatRoomsController implements LoadMyChatRoomsApiSpec {

    private final LoadMyChatRoomsUseCase loadMyChatRoomsUseCase;

    @Override
    public ResponseEntity<BaseResponse<List<ChatRoomSummary>>> loadMyChatRooms(
            @CurrentUser String userNo
    ) {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadMyChatRoomsUseCase.execute(userNo)));
    }
}
