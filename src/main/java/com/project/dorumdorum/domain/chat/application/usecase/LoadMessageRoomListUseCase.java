package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessageRoomResponse;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.pagination.CursorCodec;
import com.project.dorumdorum.global.pagination.CursorPage;
import com.project.dorumdorum.global.pagination.DecodedCursor;
import com.project.dorumdorum.global.pagination.PaginationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMessageRoomListUseCase {

    private final UserService userService;
    private final MessageRoomService messageRoomService;
    private final Integer limit = 50;

    public CursorPage<LoadMessageRoomResponse> execute(String userNo, String cursor) {
        // 유저 검증
        userService.validateExistsById(userNo);

        // 리밋 + 1
        int limitPlusOne = PaginationHelper.limitPlusOne(limit);

        // 커서 디코딩
        DecodedCursor decodedCursor = cursor == null
            ? null
            : CursorCodec.decode(cursor);

        // 조회
        List<LoadMessageRoomResponse> responses = messageRoomService.findByCursor(
            userNo,
            decodedCursor,
            limitPlusOne
        );

        return buildCursorPageFromResponses(responses);
    }

    private CursorPage<LoadMessageRoomResponse> buildCursorPageFromResponses(List<LoadMessageRoomResponse> responses) {
        boolean hasNext = responses.size() > limit;
        List<LoadMessageRoomResponse> slice = hasNext ? responses.subList(0, limit) : responses;

        String nextCursor = null;
        if (hasNext && !slice.isEmpty()) {
            LoadMessageRoomResponse last = slice.get(slice.size() - 1);
            if (last.lastMessageAt() != null) {
                nextCursor = CursorCodec.encode(last.lastMessageAt(), last.messageRoomNo());
            }
        }

        return PaginationHelper.toCursorPage(
            slice,
            limit,
            nextCursor
        );
    }
}
