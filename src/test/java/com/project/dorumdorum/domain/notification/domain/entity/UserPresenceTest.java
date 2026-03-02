package com.project.dorumdorum.domain.notification.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserPresence 값 객체 단위 테스트")
class UserPresenceTest {

    @Test
    @DisplayName("offline/online/inChatroom 정적 팩토리는 올바른 kind와 roomNo를 가진다")
    void factoryMethods_CreateExpectedStates() {
        UserPresence offline = UserPresence.offline();
        UserPresence online = UserPresence.online();
        UserPresence inChat = UserPresence.inChatroom("room-1");

        assertThat(offline.kind()).isEqualTo(UserPresence.PresenceKind.OFFLINE);
        assertThat(offline.messageRoomNo()).isNull();

        assertThat(online.kind()).isEqualTo(UserPresence.PresenceKind.ONLINE);
        assertThat(online.messageRoomNo()).isNull();

        assertThat(inChat.kind()).isEqualTo(UserPresence.PresenceKind.IN_CHATROOM);
        assertThat(inChat.messageRoomNo()).isEqualTo("room-1");
    }

    @Test
    @DisplayName("toRedisValue/fromRedisValue는 ONLINE/OFFLINE/IN_CHATROOM 값을 상호 변환한다")
    void toRedisValue_And_fromRedisValue_RoundTrip() {
        UserPresence offline = UserPresence.offline();
        UserPresence online = UserPresence.online();
        UserPresence inChat = UserPresence.inChatroom("room-1");

        assertThat(UserPresence.fromRedisValue(offline.toRedisValue()))
                .isEqualTo(offline);
        assertThat(UserPresence.fromRedisValue(online.toRedisValue()))
                .isEqualTo(online);
        assertThat(UserPresence.fromRedisValue(inChat.toRedisValue()))
                .isEqualTo(inChat);
    }

    @Test
    @DisplayName("fromRedisValue는 비어있거나 알 수 없는 값이면 OFFLINE으로 처리한다")
    void fromRedisValue_UnknownOrBlank_ReturnsOffline() {
        assertThat(UserPresence.fromRedisValue(null).kind())
                .isEqualTo(UserPresence.PresenceKind.OFFLINE);
        assertThat(UserPresence.fromRedisValue("").kind())
                .isEqualTo(UserPresence.PresenceKind.OFFLINE);
        assertThat(UserPresence.fromRedisValue("UNKNOWN").kind())
                .isEqualTo(UserPresence.PresenceKind.OFFLINE);
    }

    @Test
    @DisplayName("fromRedisValue는 IN_CHATROOM 프리픽스지만 roomNo가 비어있으면 ONLINE으로 처리한다")
    void fromRedisValue_InChatroomWithoutRoomNo_ReturnsOnline() {
        UserPresence presence = UserPresence.fromRedisValue("IN_CHATROOM:   ");
        assertThat(presence.kind()).isEqualTo(UserPresence.PresenceKind.ONLINE);
    }
}

