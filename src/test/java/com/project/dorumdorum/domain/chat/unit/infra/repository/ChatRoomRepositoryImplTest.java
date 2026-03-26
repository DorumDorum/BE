package com.project.dorumdorum.domain.chat.unit.infra.repository;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.infra.repository.ChatRoomRepositoryImpl;
import com.querydsl.core.types.Expression;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatRoomRepositoryImpl Unit Tests")
class ChatRoomRepositoryImplTest {

    @Mock private JPAQueryFactory queryFactory;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("findMyChatRooms: 결과 목록을 반환한다")
    void findMyChatRooms_ReturnsList() {
        ChatRoomRepositoryImpl repository = new ChatRoomRepositoryImpl(queryFactory);
        JPAQuery<ChatRoomSummary> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        List<ChatRoomSummary> expected = List.of(
                new ChatRoomSummary("cr-1", "room-1", ChatRoomType.GROUP, null, null, null, "안녕", LocalDateTime.now(), 3L)
        );

        when(queryFactory.select(any(Expression.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expected);

        List<ChatRoomSummary> result = repository.findMyChatRooms("user-1");

        assertThat(result).isEqualTo(expected);
        verify(queryFactory).select(any(Expression.class));
        verify(jpaQuery).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("findMyChatRooms: 참여 채팅방이 없으면 빈 목록을 반환한다")
    void findMyChatRooms_WhenNone_ReturnsEmpty() {
        ChatRoomRepositoryImpl repository = new ChatRoomRepositoryImpl(queryFactory);
        JPAQuery<ChatRoomSummary> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        when(queryFactory.select(any(Expression.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(List.of());

        List<ChatRoomSummary> result = repository.findMyChatRooms("user-1");

        assertThat(result).isEmpty();
        verify(jpaQuery).fetch();
    }
}
