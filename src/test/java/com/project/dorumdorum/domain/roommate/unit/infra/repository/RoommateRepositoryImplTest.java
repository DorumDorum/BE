package com.project.dorumdorum.domain.roommate.unit.infra.repository;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.infra.repository.RoommateRepositoryImpl;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoommateRepositoryImpl Unit Tests")
class RoommateRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock(answer = Answers.RETURNS_SELF)
    @SuppressWarnings("rawtypes")
    private JPAQuery query;

    @Test
    @DisplayName("Should return fetched roommates from query chain")
    @SuppressWarnings({"unchecked", "rawtypes"})
    void findMyRoommates_ReturnsFetchedResults() {
        List<MyRoommateResponse> expected = List.of(
                new MyRoommateResponse("rm1", "u1", ConfirmStatus.PENDING, RoomRole.HOST,
                        "name", "nick", "20210001", "major", "3", 25, Gender.MALE, true)
        );

        when(queryFactory.select(org.mockito.ArgumentMatchers.<Expression<?>>any())).thenReturn(query);
        when(query.fetch()).thenReturn(expected);

        RoommateRepositoryImpl repository = new RoommateRepositoryImpl(queryFactory);
        List<MyRoommateResponse> result = repository.findMyRoommates("u1");

        assertThat(result).isEqualTo(expected);
        verify(queryFactory).select(org.mockito.ArgumentMatchers.<Expression<?>>any());
        verify(query).fetch();
    }
}
