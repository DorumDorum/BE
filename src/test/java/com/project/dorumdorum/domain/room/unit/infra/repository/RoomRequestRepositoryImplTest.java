package com.project.dorumdorum.domain.room.unit.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.infra.repository.RoomRequestRepositoryImpl;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomRequestRepositoryImpl Unit Tests")
class RoomRequestRepositoryImplTest {

    @Mock private JPAQueryFactory queryFactory;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch application responses by room")
    void findApplicationsByRoom_FetchesList() {
        RoomRequestRepositoryImpl repository = new RoomRequestRepositoryImpl(queryFactory);
        JPAQuery<RoomRequestApplicationResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        Room room = Room.builder().roomNo("r1").build();
        List<RoomRequestApplicationResponse> expected = List.of(
                RoomRequestApplicationResponse.builder()
                        .requestNo("rq1").userNo("u1").name("name").nickname("nick")
                        .major("major").studentNo("2021001").grade("3").age(25)
                        .createdAt(LocalDateTime.now()).introduction("intro").additionalMessage("msg")
                        .build()
        );

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<RoomRequestApplicationResponse>>any()))
                .thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expected);

        List<RoomRequestApplicationResponse> result = repository.findApplicationsByRoom(room);

        assertThat(result).isEqualTo(expected);
    }
}
