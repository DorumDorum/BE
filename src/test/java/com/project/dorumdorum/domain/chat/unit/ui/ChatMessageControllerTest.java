package com.project.dorumdorum.domain.chat.unit.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.SendChatMessageRequest;
import com.project.dorumdorum.domain.chat.application.usecase.SendGroupChatMessageUseCase;
import com.project.dorumdorum.domain.chat.ui.ChatMessageController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatMessageController Unit Tests")
class ChatMessageControllerTest {

    @Mock private SendGroupChatMessageUseCase useCase;
    @InjectMocks private ChatMessageController controller;

    @Test
    @DisplayName("세션의 userNo를 추출하여 UseCase를 호출한다")
    void send_ExtractsUserNoAndDelegatesToUseCase() {
        SimpMessageHeaderAccessor accessor = mock(SimpMessageHeaderAccessor.class);
        when(accessor.getSessionAttributes()).thenReturn(Map.of("userNo", "user-1"));

        controller.send("cr-1", new SendChatMessageRequest("hello"), accessor);

        verify(useCase).send("cr-1", "user-1", "hello");
    }
}
