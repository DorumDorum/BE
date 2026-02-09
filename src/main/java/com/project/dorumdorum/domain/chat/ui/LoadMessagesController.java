package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.LoadMessagesRequest;
import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessagesResponse;
import com.project.dorumdorum.domain.chat.application.usecase.LoadMessagesUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.LoadMessagesApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMessagesController implements LoadMessagesApiSpec {

    private final LoadMessagesUseCase loadMessagesUseCase;

    @GetMapping("/api/message-rooms/{messageRoomNo}/messages")
    public BaseResponse<LoadMessagesResponse> loadMessages(
            @CurrentUser String userId,
            @PathVariable String messageRoomNo,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) Integer size
    ) {
        LoadMessagesRequest request = new LoadMessagesRequest(cursor, size);
        LoadMessagesResponse response = loadMessagesUseCase.execute(userId, messageRoomNo, request);
        return BaseResponse.onSuccess(response);
    }
}
