package com.project.dorumdorum.domain.notification.infra.repository;

import com.project.dorumdorum.domain.notification.domain.vo.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RedisUserDeviceTokenRepository 단위 테스트")
class RedisUserDeviceTokenRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private SetOperations<String, String> setOperations;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisUserDeviceTokenRepository repository;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("save는 유저 디바이스 세트에 deviceId를 추가하고 토큰을 TTL과 함께 저장한다")
    void save_SavesDeviceIdAndToken() {
        // when
        repository.save("user-1", "device-1", "token-1");

        // then
        verify(setOperations).add("notification:user:devices:user-1", "device-1");
        verify(valueOperations).set(eq("notification:device:user-1:device-1"), eq("token-1"), org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("getDevices는 저장된 deviceId와 토큰으로 Device 리스트를 반환한다")
    void getDevices_ReturnsDevices() {
        // given
        when(setOperations.members("notification:user:devices:user-1"))
                .thenReturn(Set.of("device-1", "device-2"));
        when(valueOperations.get("notification:device:user-1:device-1")).thenReturn("t1");
        when(valueOperations.get("notification:device:user-1:device-2")).thenReturn("t2");

        // when
        List<Device> devices = repository.getDevices("user-1");

        // then
        assertThat(devices).containsExactlyInAnyOrder(
                new Device("device-1", "t1"),
                new Device("device-2", "t2")
        );
    }

    @Test
    @DisplayName("getDevices는 토큰이 없는 deviceId는 set에서 제거한다")
    void getDevices_RemovesStaleDeviceIds() {
        // given
        when(setOperations.members("notification:user:devices:user-1"))
                .thenReturn(Set.of("device-1"));
        when(valueOperations.get("notification:device:user-1:device-1")).thenReturn(null);

        // when
        List<Device> devices = repository.getDevices("user-1");

        // then
        assertThat(devices).isEmpty();
        verify(setOperations).remove("notification:user:devices:user-1", "device-1");
    }

    @Test
    @DisplayName("remove는 device 키와 userDevices 세트에서 항목을 제거한다")
    void remove_DeletesKeys() {
        // when
        repository.remove("user-1", "device-1");

        // then
        verify(redisTemplate).delete("notification:device:user-1:device-1");
        verify(setOperations).remove("notification:user:devices:user-1", "device-1");
    }
}

