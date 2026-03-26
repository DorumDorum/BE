package com.project.dorumdorum.domain.chat.application.dto.response;

public record ChatRoomMemberResponse(
        String userNo,
        String nickname,
        boolean isHost
) {}
