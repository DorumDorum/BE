package com.project.dorumdorum.domain.chat.application.dto.response;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;

import java.time.LocalDateTime;

public record ChatRoomSummary(
        String chatRoomNo,
        String roomNo,
        ChatRoomType chatRoomType,
        /** DIRECT 채팅방일 때 상대방 userNo (GROUP이면 null) */
        String partnerUserNo,
        /** DIRECT 채팅방일 때 상대방 닉네임 (GROUP이면 null) */
        String partnerNickname,
        /** GROUP 채팅방 제목 (Room.title) */
        String roomName,
        String lastMessageContent,
        LocalDateTime lastMessageAt,
        long unreadCount
) {}
