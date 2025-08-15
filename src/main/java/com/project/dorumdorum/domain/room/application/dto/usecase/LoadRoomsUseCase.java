package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.LoadRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.global.pagination.CursorCodec;
import com.project.dorumdorum.global.pagination.CursorPage;
import com.project.dorumdorum.global.pagination.PaginationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadRoomsUseCase {

    private final RoomService roomService;
    private final Integer limit = 50;

    public CursorPage<LoadRoomsResponse> execute(
            Long userNo,
            RoomRelation relation,
            List<Tag> tags,
            RoomType type,
            Integer capacity,
            RoomSort sort,
            Integer cursor
    ) {
        int limitPlusOne = PaginationHelper.limitPlusOne(limit);
        List<Room> rooms = roomService.findByCursor(relation, tags, type, capacity, sort, cursor);

        String nextCursor = rooms.isEmpty() ? null :
                CursorCodec.encode(rooms.get(rooms.size() - 1).getCreatedAt(),
                        rooms.get(rooms.size() - 1).getRoomNo());

        return PaginationHelper.toCursorPage(
                rooms.stream().map(LoadRoomsResponse::from).toList(),
                limit,
                nextCursor
    }
}
