package com.project.dorumdorum.domain.chat.unit.entity;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatRoom Entity Unit Tests")
class ChatRoomTest {

    private ChatRoom newChatRoom() {
        return ChatRoom.builder()
                .roomNo("room-1")
                .build();
    }

    @Test
    @DisplayName("빌더: roomNo가 올바르게 설정된다")
    void builder_SetsRoomNo() {
        ChatRoom chatRoom = newChatRoom();

        assertThat(chatRoom.getRoomNo()).isEqualTo("room-1");
        assertThat(chatRoom.getLastMessageContent()).isNull();
        assertThat(chatRoom.getLastMessageAt()).isNull();
        assertThat(chatRoom.getLastSenderNo()).isNull();
    }

    @Test
    @DisplayName("updateLastMessage: 세 필드를 모두 갱신한다")
    void updateLastMessage_UpdatesAllCachingFields() {
        ChatRoom chatRoom = newChatRoom();
        LocalDateTime sentAt = LocalDateTime.of(2026, 3, 19, 10, 0);

        chatRoom.updateLastMessage("안녕하세요", "user-1", sentAt);

        assertThat(chatRoom.getLastMessageContent()).isEqualTo("안녕하세요");
        assertThat(chatRoom.getLastSenderNo()).isEqualTo("user-1");
        assertThat(chatRoom.getLastMessageAt()).isEqualTo(sentAt);
    }

    @Test
    @DisplayName("updateLastMessage: 더 최신 sentAt이면 덮어쓴다")
    void updateLastMessage_OverwritesOnNewerSentAt() {
        ChatRoom chatRoom = newChatRoom();
        LocalDateTime first = LocalDateTime.of(2026, 3, 19, 10, 0);
        LocalDateTime later = LocalDateTime.of(2026, 3, 19, 11, 0);

        chatRoom.updateLastMessage("첫 메시지", "user-1", first);
        chatRoom.updateLastMessage("두 번째 메시지", "user-2", later);

        assertThat(chatRoom.getLastMessageContent()).isEqualTo("두 번째 메시지");
        assertThat(chatRoom.getLastSenderNo()).isEqualTo("user-2");
        assertThat(chatRoom.getLastMessageAt()).isEqualTo(later);
    }

    @Test
    @DisplayName("updateLastMessage: 더 오래된 sentAt이면 기존 값을 유지한다")
    void updateLastMessage_DoesNotOverwrite_WhenOlderSentAt() {
        ChatRoom chatRoom = newChatRoom();
        LocalDateTime newer = LocalDateTime.of(2026, 3, 19, 11, 0);
        LocalDateTime older = LocalDateTime.of(2026, 3, 19, 10, 0);

        chatRoom.updateLastMessage("최신 메시지", "user-2", newer);
        chatRoom.updateLastMessage("오래된 메시지", "user-1", older);

        assertThat(chatRoom.getLastMessageContent()).isEqualTo("최신 메시지");
        assertThat(chatRoom.getLastSenderNo()).isEqualTo("user-2");
        assertThat(chatRoom.getLastMessageAt()).isEqualTo(newer);
    }

    @Test
    @DisplayName("updateLastMessage: 동일 sentAt이면 기존 값을 유지한다")
    void updateLastMessage_DoesNotOverwrite_WhenSameSentAt() {
        ChatRoom chatRoom = newChatRoom();
        LocalDateTime same = LocalDateTime.of(2026, 3, 19, 10, 0);

        chatRoom.updateLastMessage("첫 메시지", "user-1", same);
        chatRoom.updateLastMessage("두 번째 메시지", "user-2", same);

        assertThat(chatRoom.getLastMessageContent()).isEqualTo("첫 메시지");
        assertThat(chatRoom.getLastSenderNo()).isEqualTo("user-1");
        assertThat(chatRoom.getLastMessageAt()).isEqualTo(same);
    }
}
