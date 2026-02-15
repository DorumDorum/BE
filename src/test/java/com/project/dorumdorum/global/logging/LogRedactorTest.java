package com.project.dorumdorum.global.logging;

import com.project.dorumdorum.global.properties.LoggingMaskingProperties;
import com.project.dorumdorum.global.properties.LoggingPolicyProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LogRedactorTest {

    private final LogRedactor logRedactor = new LogRedactor(
            new LoggingMaskingProperties(List.of("password", "token")),
            new LoggingPolicyProperties(5, 7, false)
    );

    @Test
    @DisplayName("민감 키워드 포함 문자열은 마스킹한다")
    void redactText_WithSensitiveKeyword_ReturnsMasked() {
        String masked = logRedactor.redactText("password=secret");
        assertThat(masked).isEqualTo("[MASKED]");
    }

    @Test
    @DisplayName("인자 문자열은 최대 길이로 잘린다")
    void redactArgs_WithLongText_TruncatesLength() {
        String args = logRedactor.redactArgs(new Object[]{"123456789"});
        assertThat(args).isEqualTo("[12345...]");
    }

    @Test
    @DisplayName("결과 문자열은 최대 길이로 잘린다")
    void redactResult_WithLongText_TruncatesLength() {
        String result = logRedactor.redactResult("123456789");
        assertThat(result).isEqualTo("1234567...");
    }
}
