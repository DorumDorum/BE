package com.project.dorumdorum.domain.notification.domain.entity;

/**
 * 유저 접속 상태 (값 타입). Redis 직렬화/역직렬화 포함.
 */
public record UserPresence(
        PresenceKind kind,
        String messageRoomNo
) {
    private static final String PREFIX_IN_CHATROOM = "IN_CHATROOM:";

    public static UserPresence offline() {
        return new UserPresence(PresenceKind.OFFLINE, null);
    }

    public static UserPresence online() {
        return new UserPresence(PresenceKind.ONLINE, null);
    }

    public static UserPresence inChatroom(String messageRoomNo) {
        return new UserPresence(PresenceKind.IN_CHATROOM, messageRoomNo);
    }

    public static UserPresence fromRedisValue(String value) {
        if (value == null || value.isBlank()) {
            return offline();
        } else if ("ONLINE".equals(value)) {
            return online();
        } else if (value.startsWith(PREFIX_IN_CHATROOM)) {
            String roomNo = value.substring(PREFIX_IN_CHATROOM.length()).trim();
            return roomNo.isEmpty() ? online() : inChatroom(roomNo);
        } else {
            return offline();
        }
    }

    public String toRedisValue() {
        return kind == PresenceKind.IN_CHATROOM && messageRoomNo != null
                ? PREFIX_IN_CHATROOM + messageRoomNo
                : kind.name();
    }

    public enum PresenceKind {
        OFFLINE,
        ONLINE,
        IN_CHATROOM
    }
}
