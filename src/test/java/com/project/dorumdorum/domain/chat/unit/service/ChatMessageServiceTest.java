package com.project.dorumdorum.domain.chat.unit.service;

import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.repository.ChatMessageRepository;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatMessageService Unit Tests")
class ChatMessageServiceTest {

    @Mock private ChatMessageRepository chatMessageRepository;
    @InjectMocks private ChatMessageService service;

    private final ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();

    @Test
    @DisplayName("save: ChatMessage를 저장하고 반환")
    void save_SavesAndReturnsChatMessage() {
        ChatMessage saved = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderNo("user-1")
                .content("안녕하세요")
                .messageType(MessageType.TEXT)
                .unreadCount(2)
                .build();
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(saved);

        ChatMessage result = service.save(chatRoom, "user-1", "안녕하세요", MessageType.TEXT, 2);

        assertThat(result.getContent()).isEqualTo("안녕하세요");
        assertThat(result.getUnreadCount()).isEqualTo(2);
        assertThat(result.getMessageType()).isEqualTo(MessageType.TEXT);
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }

    @Test
    @DisplayName("decreaseUnreadCount: repository에 위임하고 수정 건수 반환")
    void decreaseUnreadCount_DelegatesToRepository() {
        LocalDateTime from = LocalDateTime.of(2026, 3, 19, 12, 0);
        when(chatMessageRepository.decreaseUnreadCount("cr-1", from)).thenReturn(5);

        int updated = service.decreaseUnreadCount("cr-1", from);

        assertThat(updated).isEqualTo(5);
        verify(chatMessageRepository).decreaseUnreadCount("cr-1", from);
    }

    @Test
    @DisplayName("findMessages: repository에 위임하고 목록 반환")
    void findMessages_DelegatesToRepository() {
        List<ChatMessage> messages = List.of(
                ChatMessage.builder().chatRoom(chatRoom).senderNo("user-1")
                        .content("hi").messageType(MessageType.TEXT).unreadCount(1).build()
        );
        LocalDateTime joinedAt = LocalDateTime.of(2026, 3, 1, 0, 0);
        when(chatMessageRepository.findMessages(eq("cr-1"), eq(joinedAt), eq(null), eq(null), eq(31)))
                .thenReturn(messages);

        List<ChatMessage> result = service.findMessages("cr-1", joinedAt, null, null, 31);

        assertThat(result).isEqualTo(messages);
    }
}
