package com.project.dorumdorum.domain.room.unit.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.infra.repository.RoomRepositoryImpl;
import com.project.dorumdorum.global.pagination.DecodedCursor;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_SELF;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomRepositoryImpl Unit Tests")
class RoomRepositoryImplTest {

    @Mock private JPAQueryFactory queryFactory;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return empty list when relation is not recruiting")
    void searchByCursor_WhenRelationNotRecruiting_ReturnsEmpty() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        when(queryFactory.select(any(Expression.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(List.of());

        List<FindRoomsResponse> result = repository.searchByCursor(
                RoomRelation.APPLIED, null, null, null, RoomSort.CREATED_AT, null, 10
        );

        assertThat(result).isEmpty();
        verify(queryFactory).select(any(Expression.class));
        verify(jpaQuery).limit(10L);
        verify(jpaQuery).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch room list for recruiting relation")
    void searchByCursor_WhenRecruiting_FetchesWithLimit() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        List<FindRoomsResponse> expected = List.of(
                new FindRoomsResponse("r1", RoomType.TYPE_1, 2, 1, LocalDateTime.now(),
                        "title", "host", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name())
        );

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expected);

        List<FindRoomsResponse> result = repository.searchByCursor(
                RoomRelation.RECRUITING, List.of(RoomType.TYPE_1), List.of(2), List.of(ResidencePeriod.SEMESTER),
                RoomSort.REMAINING, new DecodedCursor(1, LocalDateTime.now(), "r1"), 51
        );

        assertThat(result).isEqualTo(expected);
        verify(jpaQuery).limit(51L);
        verify(jpaQuery).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch room list with created-at sort cursor")
    void searchByCursor_WithCreatedAtSortCursor_Fetches() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(List.of());

        List<FindRoomsResponse> result = repository.searchByCursor(
                RoomRelation.RECRUITING, null, null, null,
                RoomSort.CREATED_AT, new DecodedCursor(null, LocalDateTime.now(), "r9"), 11
        );

        assertThat(result).isEmpty();
        verify(jpaQuery).limit(11L);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch room list with null cursor and empty filters")
    void searchByCursor_WithNullCursorAndEmptyFilters_Fetches() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(List.of());

        List<FindRoomsResponse> result = repository.searchByCursor(
                RoomRelation.RECRUITING, List.of(), List.of(), List.of(),
                RoomSort.CREATED_AT, null, 7
        );

        assertThat(result).isEmpty();
        verify(jpaQuery).limit(7L);
        verify(jpaQuery).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch room list with null sort using default branch")
    void searchByCursor_WithNullSort_UsesDefaultOrderBranch() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(List.of());

        List<FindRoomsResponse> result = repository.searchByCursor(
                RoomRelation.RECRUITING, null, null, null,
                null, new DecodedCursor(null, LocalDateTime.now(), "r3"), 5
        );

        assertThat(result).isEmpty();
        verify(jpaQuery).limit(5L);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return optional result for my room query")
    void findMyRoom_ReturnsOptional() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        FindRoomsResponse response = new FindRoomsResponse("r1", RoomType.TYPE_1, 2, 1, LocalDateTime.now(),
                "title", "host", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name());

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetchOne()).thenReturn(response);

        Optional<FindRoomsResponse> result = repository.findMyRoom("u1");

        assertThat(result).contains(response);
        verify(jpaQuery).fetchOne();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return empty optional when my room does not exist")
    void findMyRoom_WhenMissing_ReturnsEmpty() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetchOne()).thenReturn(null);

        Optional<FindRoomsResponse> result = repository.findMyRoom("u1");

        assertThat(result).isEmpty();
    }
}
