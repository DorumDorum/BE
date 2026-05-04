package com.project.dorumdorum.domain.chat.application.dto.response;

public record NotificationMessage(
        NotificationType type,
        String roomNo,
        String chatRoomNo
) {
    public static NotificationMessage roomDeleted(String roomNo, String chatRoomNo) {
        return new NotificationMessage(NotificationType.ROOM_DELETED, roomNo, chatRoomNo);
    }

    public static NotificationMessage kicked(String roomNo, String chatRoomNo) {
        return new NotificationMessage(NotificationType.KICKED_FROM_ROOM, roomNo, chatRoomNo);
    }
}
