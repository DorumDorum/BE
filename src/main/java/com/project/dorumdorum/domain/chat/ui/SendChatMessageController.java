package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.SendChatMessageRequest;
import com.project.dorumdorum.domain.chat.application.usecase.SendChatMessageUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.SendChatMessageApiSpec;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SendChatMessageController implements SendChatMessageApiSpec {

    private final SendChatMessageUseCase sendChatMessageUseCase;

    @Override
    public ResponseEntity<SendChatMessageResponse> sendChatMessage(@RequestBody @Valid SendChatMessageRequest request) {
        String messageNo = sendChatMessageUseCase.execute(request);
        return ResponseEntity
                .created(URI.create("/api/chat/messages/" + messageNo))
                .body(new SendChatMessageResponse(messageNo));
    }
}
