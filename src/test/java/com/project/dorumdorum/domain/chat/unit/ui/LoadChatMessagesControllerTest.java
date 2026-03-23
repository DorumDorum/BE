package com.project.dorumdorum.domain.chat.unit.ui;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageSummary;
import com.project.dorumdorum.domain.chat.application.usecase.LoadChatMessagesUseCase;
import com.project.dorumdorum.domain.chat.ui.LoadChatMessagesController;
import com.project.dorumdorum.global.pagination.CursorPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadChatMessagesController Unit Tests")
class LoadChatMessagesControllerTest {

    @Mock private LoadChatMessagesUseCase useCase;
    @InjectMocks private LoadChatMessagesController controller;

    @Test
    @DisplayName("UseCase 결과를 그대로 반환한다")
    void loadChatMessages_ReturnsCursorPage() {
        CursorPage<ChatMessageSummary> page = new CursorPage<>(List.of(), null, false);
        when(useCase.execute("cr-1", "u1", null)).thenReturn(page);

        ResponseEntity<CursorPage<ChatMessageSummary>> response =
                controller.loadChatMessages("u1", "cr-1", null);

        verify(useCase).execute("cr-1", "u1", null);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(page);
    }
}
