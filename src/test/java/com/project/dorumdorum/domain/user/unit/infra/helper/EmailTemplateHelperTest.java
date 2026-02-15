package com.project.dorumdorum.domain.user.unit.infra.helper;

import com.project.dorumdorum.domain.user.infra.helper.EmailTemplateHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EmailTemplateHelper Unit Tests")
class EmailTemplateHelperTest {

    @Test
    @DisplayName("Should generate email content with code in body")
    void generate_ReturnsContentContainingVerificationCode() {
        // Arrange
        String code = "654321";

        // Act
        EmailTemplateHelper.EmailContent content = EmailTemplateHelper.generate(code);

        // Assert
        assertThat(content).isNotNull();
        assertThat(content.subject()).isEqualTo("[도룸도룸] 이메일 인증번호 안내");
        assertThat(content.body()).contains(code);
        assertThat(content.body()).contains("<html lang=\"ko\">");
    }
}
