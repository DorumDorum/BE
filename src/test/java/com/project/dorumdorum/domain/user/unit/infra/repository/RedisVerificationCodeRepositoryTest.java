package com.project.dorumdorum.domain.user.unit.infra.repository;

import com.project.dorumdorum.domain.user.infra.repository.RedisVerificationCodeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RedisVerificationCodeRepository Unit Tests")
class RedisVerificationCodeRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisVerificationCodeRepository repository;

    @Test
    @DisplayName("Should save verification code with 10 minute expiration")
    void save_StoresCodeWithExpiration() {
        String email = "test@university.ac.kr";
        String code = "123456";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        repository.save(email, code);

        verify(valueOperations).set("EMAIL_VERIFICATION:" + email, code, Duration.ofMinutes(10));
    }

    @Test
    @DisplayName("Should return code when email exists")
    void findByEmail_WhenPresent_ReturnsOptionalWithCode() {
        String email = "test@university.ac.kr";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("EMAIL_VERIFICATION:" + email)).thenReturn("123456");

        var result = repository.findByEmail(email);

        assertThat(result).contains("123456");
    }

    @Test
    @DisplayName("Should return empty when email does not exist")
    void findByEmail_WhenAbsent_ReturnsEmpty() {
        String email = "unknown@university.ac.kr";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("EMAIL_VERIFICATION:" + email)).thenReturn(null);

        var result = repository.findByEmail(email);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should delete verification code by email")
    void delete_RemovesStoredCode() {
        String email = "test@university.ac.kr";

        repository.delete(email);

        verify(redisTemplate).delete("EMAIL_VERIFICATION:" + email);
    }
}
