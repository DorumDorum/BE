package com.project.dorumdorum.domain.user.infra.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailTemplateHelper {

    public static EmailContent generate(String code) {
        String subject = "[도룸도룸] 이메일 인증번호";
        String body    = "인증번호는 " + code + " 입니다. 10분 안에 입력해 주세요.";
        return new EmailContent(subject, body);
    }

    public record EmailContent(
            String subject,
            String body
    ) {}
}
