package com.project.dorumdorum.domain.chat.unit.entity;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatRoomMember Entity Unit Tests")
class ChatRoomMemberTest {

    private final ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();

    private ChatRoomMember newMember() {
        return ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .userNo("user-1")
                .build();
    }

    @Test
    @DisplayName("빌더: chatRoom, userNo가 올바르게 설정된다")
    void builder_SetsFields() {
        ChatRoomMember member = newMember();

        assertThat(member.getChatRoom()).isEqualTo(chatRoom);
        assertThat(member.getUserNo()).isEqualTo("user-1");
        assertThat(member.getLastReadAt()).isNull();
    }

    @Test
    @DisplayName("init: @PrePersist 시 joinedAt이 현재 시각으로 설정된다")
    void init_SetsJoinedAt() {
        ChatRoomMember member = newMember();
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        member.init();

        assertThat(member.getJoinedAt()).isAfterOrEqualTo(before);
        assertThat(member.getJoinedAt()).isBeforeOrEqualTo(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    @DisplayName("updateLastReadAt: lastReadAt이 전달된 시각으로 갱신된다")
    void updateLastReadAt_UpdatesField() {
        ChatRoomMember member = newMember();
        LocalDateTime readAt = LocalDateTime.of(2026, 3, 19, 12, 30);

        member.updateLastReadAt(readAt);

        assertThat(member.getLastReadAt()).isEqualTo(readAt);
    }

    @Test
    @DisplayName("updateLastReadAt: 연속 호출 시 최신 값으로 덮어쓴다")
    void updateLastReadAt_OverwritesOnSecondCall() {
        ChatRoomMember member = newMember();
        member.updateLastReadAt(LocalDateTime.of(2026, 3, 19, 10, 0));
        LocalDateTime later = LocalDateTime.of(2026, 3, 19, 15, 0);

        member.updateLastReadAt(later);

        assertThat(member.getLastReadAt()).isEqualTo(later);
    }
}
