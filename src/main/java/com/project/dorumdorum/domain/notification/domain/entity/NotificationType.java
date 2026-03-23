package com.project.dorumdorum.domain.notification.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    
    ROOM_APPLICATION_APPROVED("/rooms/me"),
    ROOM_APPLICATION_REJECTED("/rooms/search"),
    ROOM_APPLICATION_RECEIVED("/rooms/me"),
    CHAT_MESSAGE_REQUEST("/chats/{chatRoomNo}"),
    CHAT_REQUEST_APPROVED("/chats/{chatRoomNo}"),
    CHAT_REQUEST_REJECTED("/chats/{chatRoomNo}"),
    NEW_MESSAGE_RECEIVED("/chats/{chatRoomNo}");

    private final String pathTemplate;

    public boolean isChatNotification() {
        return this == CHAT_MESSAGE_REQUEST || this == CHAT_REQUEST_APPROVED
                || this == CHAT_REQUEST_REJECTED || this == NEW_MESSAGE_RECEIVED;
    }
}
