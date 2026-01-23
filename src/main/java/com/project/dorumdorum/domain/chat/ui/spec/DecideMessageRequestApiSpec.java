package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.request.DecideMessageRequest;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "Message", description = "채팅/메시지 전송 API")
public interface DecideMessageRequestApiSpec {

    @Operation(
            summary = "메시지 전송 요청 수락/거절",
            description = "메시지 전송 요청(messageRequestNo)을 수락 또는 거절합니다. "
                    + "결정 값은 Request Body(DecideMessageRequest)로 전달합니다."
    )
    @PatchMapping("/api/chat/request/{messageRequestNo}")
    BaseResponse<Void> decideMessageRequest(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "메시지 요청 번호") Long messageRequestNo,
            @RequestBody(
                    description = "메시지 전송 요청 수락/거절",
                    required = true
            )
            DecideMessageRequest request
    );
}
