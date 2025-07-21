package com.project.dorumdorum.domain.user.infra.smtp;

import com.project.dorumdorum.global.exception.RestApiException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.FAILED_SEND_VERIFY_CODE;

@Component
@RequiredArgsConstructor
public class SmtpEmailSender {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String text) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);          // 두 번째 파라미터 true → HTML 메일

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RestApiException(FAILED_SEND_VERIFY_CODE);
        }
    }
}
