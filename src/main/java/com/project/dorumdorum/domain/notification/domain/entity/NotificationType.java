package com.project.dorumdorum.domain.notification.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    ROOM_APPLICATION_APPROVED("/rooms/me"),
    ROOM_APPLICATION_REJECTED("/rooms/search"),
    ROOM_APPLICATION_RECEIVED("/rooms/me"),
    CHAT_MESSAGE_REQUEST("/chat/{messageRoomNo}"),
    CHAT_REQUEST_APPROVED("/chat/{messageRoomNo}"),
    CHAT_REQUEST_REJECTED("/chat/{messageRoomNo}"),
    NEW_MESSAGE_RECEIVED("/chat/{messageRoomNo}");

    private final String pathTemplate;
}
