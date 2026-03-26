package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;

import java.util.List;

public interface ChatRoomQueryRepository {

    List<ChatRoomSummary> findMyChatRooms(String userNo);
}
