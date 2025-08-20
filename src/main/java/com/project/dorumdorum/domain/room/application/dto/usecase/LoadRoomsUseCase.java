package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.LoadRoomsResponse;
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

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.COMPLETED_ROOM_EXISTS;

@Service
@RequiredArgsConstructor
public class LoadRoomsUseCase {

    private final RoomService roomService;
    private final Integer limit = 50;
    private final RoommateService roommateService;

    public CursorPage<LoadRoomsResponse> execute(
            Long userNo,
            RoomRelation relation,
            List<Tag> tags,
            RoomType type,
            Integer capacity,
            RoomSort sort,
            String cursor
    ) {
        if(roommateService.isCompletedRoomExists(userNo))
            throw new RestApiException(COMPLETED_ROOM_EXISTS);

        int limitPlusOne = PaginationHelper.limitPlusOne(limit);
        DecodedCursor decodedCursor = cursor == null
                ? null
                : CursorCodec.decode(cursor);

        List<LoadRoomsResponse> responses = roomService.findByCursor(
                userNo, relation, tags, type, capacity, sort, decodedCursor, limitPlusOne
        );

        return buildCursorPageFromResponses(sort, responses);
    }

    private CursorPage<LoadRoomsResponse> buildCursorPageFromResponses(RoomSort sort, List<LoadRoomsResponse> responses) {
        boolean hasNext = responses.size() > limit;
        List<LoadRoomsResponse> slice = hasNext ? responses.subList(0, limit) : responses;

        String nextCursor = null;
        if (!slice.isEmpty()) {
            LoadRoomsResponse last = slice.get(slice.size() - 1);
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
