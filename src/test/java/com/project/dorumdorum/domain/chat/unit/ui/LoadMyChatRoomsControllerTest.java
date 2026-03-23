package com.project.dorumdorum.domain.chat.unit.ui;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.application.usecase.LoadMyChatRoomsUseCase;
import com.project.dorumdorum.domain.chat.ui.LoadMyChatRoomsController;
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
@DisplayName("LoadMyChatRoomsController Unit Tests")
class LoadMyChatRoomsControllerTest {

    @Mock private LoadMyChatRoomsUseCase useCase;
    @InjectMocks private LoadMyChatRoomsController controller;

    @Test
    @DisplayName("UseCase 결과를 그대로 반환한다")
    void loadMyChatRooms_ReturnsResult() {
        List<ChatRoomSummary> list = List.of(
                new ChatRoomSummary("cr-1", "r-1", ChatRoomType.GROUP, null, null, null, "hi", null, 1L)
        );
        when(useCase.execute("u1")).thenReturn(list);

        ResponseEntity<List<ChatRoomSummary>> response =
                controller.loadMyChatRooms("u1");

        verify(useCase).execute("u1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(list);
    }
}
