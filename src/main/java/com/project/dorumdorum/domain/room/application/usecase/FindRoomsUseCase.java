package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.global.pagination.CursorCodec;
import com.project.dorumdorum.global.pagination.CursorPage;
import com.project.dorumdorum.global.pagination.DecodedCursor;
import com.project.dorumdorum.global.pagination.PaginationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindRoomsUseCase {

    private final RoomService roomService;
    private final Integer limit = 50;

    public CursorPage<FindRoomsResponse> execute(
            RoomRelation relation,
            List<RoomType> types,
            List<Integer> capacities,
            List<ResidencePeriod> residencePeriods,
            RoomSort sort,
            String cursor
    ) {
        int limitPlusOne = PaginationHelper.limitPlusOne(limit);
        DecodedCursor decodedCursor = cursor == null
                ? null
                : CursorCodec.decode(cursor);

        List<FindRoomsResponse> responses = roomService.findByCursor(
                relation, types, capacities, residencePeriods, sort, decodedCursor, limitPlusOne
        );

        return buildCursorPageFromResponses(sort, responses);
    }

    private CursorPage<FindRoomsResponse> buildCursorPageFromResponses(RoomSort sort, List<FindRoomsResponse> responses) {
        boolean hasNext = responses.size() > limit;
        List<FindRoomsResponse> slice = hasNext ? responses.subList(0, limit) : responses;

        String nextCursor = null;
        if (!slice.isEmpty()) {
            FindRoomsResponse last = slice.get(slice.size() - 1);
            if (sort == RoomSort.REMAINING) {
                nextCursor = CursorCodec.encodeWithRemaining(
                    last.capacity() - last.currentMateCount(),
                    last.createdAt(),
                    last.roomNo()
                );
            } else {
                nextCursor = CursorCodec.encode(
                    last.createdAt(),
                    last.roomNo()
                );
            }
        }

        return PaginationHelper.toCursorPage(
                slice,
                limit,
                nextCursor
        );
    }
}
