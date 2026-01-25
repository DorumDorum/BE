package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.pagination.CursorCodec;
import com.project.dorumdorum.global.pagination.CursorPage;
import com.project.dorumdorum.global.pagination.DecodedCursor;
import com.project.dorumdorum.global.pagination.PaginationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.ALREADY_JOINED_USER;

@Service
@RequiredArgsConstructor
public class FindRoomsUseCase {

    private final RoomService roomService;
    private final Integer limit = 50;
    private final RoommateService roommateService;

    public CursorPage<FindRoomsResponse> execute(
            Long userNo,
            RoomRelation relation,
            List<Tag> tags,
            RoomType type,
            Integer capacity,
            RoomSort sort,
            String cursor
    ) {
        // 내가 이미 속한 방이 있는지
        if(roommateService.existsByUserNo(userNo))
            throw new RestApiException(ALREADY_JOINED_USER);

        int limitPlusOne = PaginationHelper.limitPlusOne(limit);
        DecodedCursor decodedCursor = cursor == null
                ? null
                : CursorCodec.decode(cursor);

        List<FindRoomsResponse> responses = roomService.findByCursor(
                userNo, relation, tags, type, capacity, sort, decodedCursor, limitPlusOne
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
