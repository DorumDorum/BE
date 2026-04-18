package com.project.dorumdorum.domain.chat.application.dto.response;

public record NotificationMessage(
        String type,
        String roomNo,
        String chatRoomNo
) {
    public static NotificationMessage roomDeleted(String roomNo, String chatRoomNo) {
        return new NotificationMessage("ROOM_DELETED", roomNo, chatRoomNo);
    }

    public static NotificationMessage kicked(String roomNo, String chatRoomNo) {
        return new NotificationMessage("KICKED_FROM_ROOM", roomNo, chatRoomNo);
    }
}
