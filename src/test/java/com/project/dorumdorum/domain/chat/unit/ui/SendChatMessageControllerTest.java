package com.project.dorumdorum.domain.chat.unit.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.SendChatMessageRequest;
import com.project.dorumdorum.domain.chat.application.usecase.SendChatMessageUseCase;
import com.project.dorumdorum.domain.chat.ui.SendChatMessageController;
import com.project.dorumdorum.domain.chat.ui.spec.SendChatMessageApiSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendChatMessageController Unit Tests")
class SendChatMessageControllerTest {

    @Mock
    private SendChatMessageUseCase sendChatMessageUseCase;

    @InjectMocks
    private SendChatMessageController controller;

    @Test
    @DisplayName("메시지 전송 시 201 Created와 messageNo 반환")
    void sendChatMessage_WithValidRequest_ReturnsCreatedAndMessageNo() {
        SendChatMessageRequest request = new SendChatMessageRequest(
                "sender-1",
                "recipient-1",
                "안녕하세요"
        );
        when(sendChatMessageUseCase.execute(request)).thenReturn("msg-123");

        ResponseEntity<SendChatMessageApiSpec.SendChatMessageResponse> response =
                controller.sendChatMessage(request);

        verify(sendChatMessageUseCase).execute(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation())
                .isEqualTo(URI.create("/api/chat/messages/msg-123"));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().messageNo()).isEqualTo("msg-123");
    }
}
