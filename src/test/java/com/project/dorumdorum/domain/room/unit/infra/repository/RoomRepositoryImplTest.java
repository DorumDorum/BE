package com.project.dorumdorum.domain.room.unit.infra.repository;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.checklist.domain.entity.enums.SmokingType;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.infra.repository.RoomRepositoryImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_SELF;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomRepositoryImpl Unit Tests")
class RoomRepositoryImplTest {

    @Mock private JPAQueryFactory queryFactory;
    @Mock private EntityManager entityManager;

    private ChecklistFilterRequest request(ChecklistFilterRequest.SortType sortType) {
        return new ChecklistFilterRequest(
                sortType, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch room list for recruiting relation")
    void findByCursor_WhenRecruiting_FetchesWithLimit() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory, entityManager);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        List<FindRoomsResponse> expected = List.of(
                new FindRoomsResponse("r1", RoomType.TYPE_1, 2, 1, LocalDateTime.now(),
                        "title", "host", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name(), 1)
        );

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expected);

        LocalDateTime cursorCreatedAt = LocalDateTime.now();
        ChecklistFilterRequest request = new ChecklistFilterRequest(
                ChecklistFilterRequest.SortType.REMAINING, null, RoomType.TYPE_1, ResidencePeriod.SEMESTER, 2,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null
        );

        List<FindRoomsResponse> result = repository.findByCursor(
                request, cursorCreatedAt, "r1", 1, 51
        );

        assertThat(result).isEqualTo(expected);
        verify(jpaQuery).limit(51L);
        verify(jpaQuery).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch room list with created-at sort cursor")
    void findByCursor_WithCreatedAtSortCursor_Fetches() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory, entityManager);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(List.of());

        LocalDateTime cursorCreatedAt = LocalDateTime.now();
        ChecklistFilterRequest request = request(ChecklistFilterRequest.SortType.LATEST);

        List<FindRoomsResponse> result = repository.findByCursor(
                request, cursorCreatedAt, "r9", null, 11
        );

        assertThat(result).isEmpty();
        verify(jpaQuery).limit(11L);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch room list with null cursor and empty filters")
    void findByCursor_WithNullCursorAndEmptyFilters_Fetches() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory, entityManager);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(List.of());
        ChecklistFilterRequest request = request(ChecklistFilterRequest.SortType.LATEST);

        List<FindRoomsResponse> result = repository.findByCursor(
                request, null, null, null, 7
        );

        assertThat(result).isEmpty();
        verify(jpaQuery).limit(7L);
        verify(jpaQuery).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should fetch room list with null sort using default branch")
    void findByCursor_WithNullSort_UsesDefaultOrderBranch() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory, entityManager);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(List.of());

        LocalDateTime cursorCreatedAt = LocalDateTime.now();
        ChecklistFilterRequest request = request(null);

        List<FindRoomsResponse> result = repository.findByCursor(
                request, cursorCreatedAt, "r3", null, 5
        );

        assertThat(result).isEmpty();
        verify(jpaQuery).limit(5L);
    }

    @Test
    @DisplayName("Should use native lateral query when checklist filters exist")
    void findByCursor_WithChecklistFilters_UsesNativeLateralQuery() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory, entityManager);
        Query nativeQuery = mock(Query.class);
        LocalDateTime createdAt = LocalDateTime.now();
        ChecklistFilterRequest request = new ChecklistFilterRequest(
                ChecklistFilterRequest.SortType.REMAINING,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                SmokingType.NON_SMOKER,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Object[] row = {
                "r1",
                RoomType.TYPE_1.name(),
                2,
                1,
                createdAt,
                "title",
                "host",
                RoomStatus.CONFIRM_PENDING.name(),
                ResidencePeriod.SEMESTER.name(),
                1
        };

        when(entityManager.createNativeQuery(anyString())).thenReturn(nativeQuery);
        when(nativeQuery.setParameter(anyString(), any())).thenReturn(nativeQuery);
        List<Object[]> rows = new ArrayList<>();
        rows.add(row);
        when(nativeQuery.getResultList()).thenReturn(rows);

        List<FindRoomsResponse> result = repository.findByCursor(request, createdAt, "r1", 1, 51);

        assertThat(result).containsExactly(
                new FindRoomsResponse("r1", RoomType.TYPE_1, 2, 1, createdAt,
                        "title", "host", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name(), 1)
        );
        verify(entityManager).createNativeQuery(anyString());
        verify(nativeQuery).getResultList();
        verifyNoInteractions(queryFactory);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return optional result for my room query")
    void findMyRoom_ReturnsOptional() {
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory, entityManager);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        FindRoomsResponse response = new FindRoomsResponse("r1", RoomType.TYPE_1, 2, 1, LocalDateTime.now(),
                "title", "host", RoomStatus.CONFIRM_PENDING, ResidencePeriod.SEMESTER.name(), 1);

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
        RoomRepositoryImpl repository = new RoomRepositoryImpl(queryFactory, entityManager);
        JPAQuery<FindRoomsResponse> jpaQuery = mock(JPAQuery.class, RETURNS_SELF);
        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<FindRoomsResponse>>any())).thenReturn(jpaQuery);
        when(jpaQuery.fetchOne()).thenReturn(null);

        Optional<FindRoomsResponse> result = repository.findMyRoom("u1");

        assertThat(result).isEmpty();
    }
}
