package com.project.dorumdorum.domain.chat.application.event;

public record MessageRoomReadStateChangedEvent(
    String messageRoomNo,
    String trigger,
    String actorUserId
) {
    public static MessageRoomReadStateChangedEvent of(String messageRoomNo, String trigger, String actorUserId) {
        return new MessageRoomReadStateChangedEvent(messageRoomNo, trigger, actorUserId);
    }
}
