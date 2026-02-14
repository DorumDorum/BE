package com.project.dorumdorum.domain.user.unit.infra.smtp;

import com.project.dorumdorum.domain.user.infra.smtp.SmtpEmailSender;
import com.project.dorumdorum.global.exception.RestApiException;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SmtpEmailSender Unit Tests")
class SmtpEmailSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private SmtpEmailSender smtpEmailSender;

    @Test
    @DisplayName("Should send HTML email successfully")
    void send_WithValidInput_SendsEmail() {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        smtpEmailSender.send("test@university.ac.kr", "subject", "<h1>body</h1>");

        verify(mailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("Should throw RestApiException when messaging exception occurs")
    void send_WhenMessagingExceptionOccurs_ThrowsRestApiException() throws Exception {
        MimeMessage mimeMessage = org.mockito.Mockito.mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MessagingException("failed"))
                .when(mimeMessage)
                .setSubject(any(String.class), any(String.class));

        assertThatThrownBy(() -> smtpEmailSender.send("test@university.ac.kr", "subject", "<h1>body</h1>"))
                .isInstanceOf(RestApiException.class);
    }
}
