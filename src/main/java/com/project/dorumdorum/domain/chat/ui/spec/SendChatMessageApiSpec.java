package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.request.SendChatMessageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Chat", description = "채팅 API")
public interface SendChatMessageApiSpec {

    @Operation(summary = "채팅 메시지 전송", description = "메시지를 저장하고 수신자에게 알림을 전송합니다.")
    @PostMapping("/api/chat/messages")
    ResponseEntity<SendChatMessageResponse> sendChatMessage(
            @RequestBody @Valid SendChatMessageRequest request
    );

    record SendChatMessageResponse(String messageNo) {}
}
