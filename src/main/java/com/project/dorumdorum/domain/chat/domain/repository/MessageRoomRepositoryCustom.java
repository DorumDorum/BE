package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessageRoomResponse;
import com.project.dorumdorum.global.pagination.DecodedCursor;

import java.util.List;

public interface MessageRoomRepositoryCustom {
    List<LoadMessageRoomResponse> findByCursor(String userNo, DecodedCursor cursor, int limitPlusOne);
}
