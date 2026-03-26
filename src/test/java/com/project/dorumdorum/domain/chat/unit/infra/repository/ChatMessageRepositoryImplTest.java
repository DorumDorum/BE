package com.project.dorumdorum.domain.chat.unit.infra.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.infra.repository.ChatMessageRepositoryImpl;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_SELF;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatMessageRepositoryImpl Unit Tests")
class ChatMessageRepositoryImplTest {

    @Mock private JPAQueryFactory queryFactory;

    private final ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("findMessages: cursor 없이 첫 페이지 목록을 반환한다")
    void findMessages_WithoutCursor_ReturnsList() {
        ChatMessageRepositoryImpl repository = new ChatMessageRepositoryImpl(queryFactory);
        JPAQuery<ChatMessage> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        ChatMessage msg = ChatMessage.builder()
                .chatRoom(chatRoom).senderNo("user-1")
                .content("hi").messageType(MessageType.TEXT).unreadCount(1).build();

        doReturn(jpaQuery).when(queryFactory).selectFrom(any());
        when(jpaQuery.fetch()).thenReturn(List.of(msg));

        LocalDateTime joinedAt = LocalDateTime.of(2026, 3, 1, 0, 0);
        List<ChatMessage> result = repository.findMessages("cr-1", joinedAt, null, null, 31);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("hi");
        verify(jpaQuery).limit(31L);
        verify(jpaQuery).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("findMessages: cursor 있으면 복합 predicate 조건이 추가된다")
    void findMessages_WithCursor_AppliesCompositeCursorPredicate() {
        ChatMessageRepositoryImpl repository = new ChatMessageRepositoryImpl(queryFactory);
        JPAQuery<ChatMessage> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        doReturn(jpaQuery).when(queryFactory).selectFrom(any());
        when(jpaQuery.fetch()).thenReturn(List.of());

        LocalDateTime joinedAt = LocalDateTime.of(2026, 3, 1, 0, 0);
        LocalDateTime cursorCreatedAt = LocalDateTime.of(2026, 3, 19, 10, 0);
        List<ChatMessage> result = repository.findMessages("cr-1", joinedAt, cursorCreatedAt, "cursor-msg-no", 31);

        assertThat(result).isEmpty();
        verify(jpaQuery).limit(31L);
        verify(jpaQuery).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("findMessages: 결과 없으면 빈 목록을 반환한다")
    void findMessages_WhenEmpty_ReturnsEmptyList() {
        ChatMessageRepositoryImpl repository = new ChatMessageRepositoryImpl(queryFactory);
        JPAQuery<ChatMessage> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        doReturn(jpaQuery).when(queryFactory).selectFrom(any());
        when(jpaQuery.fetch()).thenReturn(List.of());

        LocalDateTime joinedAt = LocalDateTime.of(2026, 3, 1, 0, 0);
        List<ChatMessage> result = repository.findMessages("cr-1", joinedAt, null, null, 31);

        assertThat(result).isEmpty();
    }
}
