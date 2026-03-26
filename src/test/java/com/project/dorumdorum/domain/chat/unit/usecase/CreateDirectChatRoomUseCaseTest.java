package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.usecase.CreateDirectChatRoomUseCase;
import com.project.dorumdorum.domain.chat.application.usecase.GetOrCreateDirectChatRoomUseCase;
import com.project.dorumdorum.domain.room.application.event.RoomApplicationSubmittedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateDirectChatRoomUseCase Unit Tests")
class CreateDirectChatRoomUseCaseTest {

    @Mock private GetOrCreateDirectChatRoomUseCase getOrCreateDirectChatRoomUseCase;
    @InjectMocks private CreateDirectChatRoomUseCase useCase;

    @Test
    @DisplayName("지원 시 DIRECT 채팅방 생성을 시도한다")
    void handle_Application_AttemptsToCreateDirectChatRoom() {
        useCase.handle(new RoomApplicationSubmittedEvent("room-1", "user-applicant", "user-host"));
        verify(getOrCreateDirectChatRoomUseCase).createIfAbsent("room-1", "user-applicant", "user-host");
    }

    @Test
    @DisplayName("동시 요청으로 DB 유니크 제약 위반 시 예외 없이 무시한다")
    void handle_WhenConcurrentCreation_IgnoresDataIntegrityViolation() {
        doThrow(new DataIntegrityViolationException("uk_chat_room_direct"))
            .when(getOrCreateDirectChatRoomUseCase).createIfAbsent(anyString(), anyString(), anyString());

        // 예외가 전파되지 않아야 한다
        useCase.handle(new RoomApplicationSubmittedEvent("room-1", "user-applicant", "user-host"));

        verify(getOrCreateDirectChatRoomUseCase).createIfAbsent("room-1", "user-applicant", "user-host");
    }
}
