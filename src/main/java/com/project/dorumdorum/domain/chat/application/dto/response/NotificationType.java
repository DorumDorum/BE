package com.project.dorumdorum.domain.chat.application.dto.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
    ROOM_DELETED("ROOM_DELETED"),
    KICKED_FROM_ROOM("KICKED_FROM_ROOM");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
