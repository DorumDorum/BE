package com.project.dorumdorum.domain.chat.unit.service;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomRepository;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatRoomService Unit Tests")
class ChatRoomServiceTest {

    @Mock private ChatRoomRepository chatRoomRepository;
    @InjectMocks private ChatRoomService service;

    @Test
    @DisplayName("create: roomNo로 ChatRoom을 저장하고 반환")
    void create_SavesAndReturnsChatRoom() {
        ChatRoom saved = ChatRoom.builder().roomNo("room-1").build();
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(saved);

        ChatRoom result = service.create("room-1");

        assertThat(result.getRoomNo()).isEqualTo("room-1");
        verify(chatRoomRepository).save(any(ChatRoom.class));
    }

    @Test
    @DisplayName("findByChatRoomNo: 존재하면 반환")
    void findByChatRoomNo_WhenExists_Returns() {
        ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();
        when(chatRoomRepository.findById("cr-1")).thenReturn(Optional.of(chatRoom));

        ChatRoom result = service.findByChatRoomNo("cr-1");

        assertThat(result).isEqualTo(chatRoom);
    }

    @Test
    @DisplayName("findByChatRoomNo: 존재하지 않으면 RestApiException")
    void findByChatRoomNo_WhenMissing_Throws() {
        when(chatRoomRepository.findById("cr-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByChatRoomNo("cr-1"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("findByRoomNo: repository에 위임하고 Optional 반환")
    void findByRoomNo_DelegatesToRepository() {
        ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();
        when(chatRoomRepository.findByRoomNo("room-1")).thenReturn(Optional.of(chatRoom));

        Optional<ChatRoom> result = service.findByRoomNo("room-1");

        assertThat(result).contains(chatRoom);
    }

    @Test
    @DisplayName("existsByRoomNo: repository에 위임")
    void existsByRoomNo_DelegatesToRepository() {
        when(chatRoomRepository.existsByRoomNo("room-1")).thenReturn(true);

        assertThat(service.existsByRoomNo("room-1")).isTrue();
    }

    @Test
    @DisplayName("updateLastMessage: ChatRoom의 캐싱 필드를 갱신")
    void updateLastMessage_UpdatesFields() {
        ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();
        LocalDateTime sentAt = LocalDateTime.of(2026, 3, 19, 12, 0);

        service.updateLastMessage(chatRoom, "안녕", "user-1", sentAt);

        assertThat(chatRoom.getLastMessageContent()).isEqualTo("안녕");
        assertThat(chatRoom.getLastSenderNo()).isEqualTo("user-1");
        assertThat(chatRoom.getLastMessageAt()).isEqualTo(sentAt);
    }

    @Test
    @DisplayName("delete: repository.delete에 위임")
    void delete_DelegatesToRepository() {
        ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();

        service.delete(chatRoom);

        verify(chatRoomRepository).delete(chatRoom);
    }

    @Test
    @DisplayName("findMyChatRooms: repository에 위임하고 목록 반환")
    void findMyChatRooms_DelegatesToRepository() {
        List<ChatRoomSummary> summaries = List.of(
                new ChatRoomSummary("cr-1", "room-1", "마지막 메시지", LocalDateTime.now(), 2L)
        );
        when(chatRoomRepository.findMyChatRooms("user-1")).thenReturn(summaries);

        List<ChatRoomSummary> result = service.findMyChatRooms("user-1");

        assertThat(result).isEqualTo(summaries);
    }
}
