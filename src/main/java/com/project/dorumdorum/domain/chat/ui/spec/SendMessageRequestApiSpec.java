package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.request.SendMessageRequest;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Message", description = "채팅/메시지 전송 API")
public interface SendMessageRequestApiSpec {

    @Operation(
            summary = "메시지 전송 요청",
            description = "수신자(receiverNo)에게 1:1 메시지 전송을 요청합니다. "
                    + "본문(텍스트/첨부 등)은 Request Body(SendMessageRequest)로 전달합니다."
    )
    @PostMapping("/api/chat/request/{receiverNo}")
    BaseResponse<Void> send(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "받는 유저 번호") String receiverNo,
            @RequestBody(
                    description = "메시지 전송 요청 바디(시작 메세지)",
                    required = true
            )
            SendMessageRequest request
    );
}
