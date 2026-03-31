package com.project.dorumdorum.domain.notification.infra.repository;

import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RedisUserPresenceRepository 단위 테스트")
class RedisUserPresenceRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisUserPresenceRepository repository;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("refreshPresence는 현재 IN_CHATROOM 상태이면 상태를 유지하고 TTL만 갱신한다")
    void refreshPresence_PreservesInChatroomPresence() {
        // given
        String userNo = "user-1";
        String key = "notification:presence:" + userNo;
        UserPresence inChatroom = UserPresence.inChatroom("room-1");
        when(valueOperations.get(key)).thenReturn(inChatroom.toRedisValue());

        // when
        assertThatCode(() -> repository.refreshPresence(userNo)).doesNotThrowAnyException();

        // then
        verify(valueOperations).set(eq(key), eq(inChatroom.toRedisValue()), any(Duration.class));
    }

    @Test
    @DisplayName("refreshPresence는 OFFLINE일 때 ONLINE 상태로 설정한다")
    void refreshPresence_SetsOnlineWhenOffline() {
        // given
        String userNo = "user-2";
        String key = "notification:presence:" + userNo;
        when(valueOperations.get(key)).thenReturn(null); // OFFLINE

        // when
        repository.refreshPresence(userNo);

        // then
        String expected = UserPresence.online().toRedisValue(); // "ONLINE"
        verify(valueOperations).set(eq(key), eq(expected), any(Duration.class));
    }

    @Test
    @DisplayName("setOnline은 현재 상태와 무관하게 ONLINE으로 전환한다")
    void setOnline_SetsOnline() {
        String userNo = "user-3";
        String key = "notification:presence:" + userNo;

        repository.setOnline(userNo);

        verify(valueOperations).set(eq(key), eq(UserPresence.online().toRedisValue()), any(Duration.class));
    }
}
