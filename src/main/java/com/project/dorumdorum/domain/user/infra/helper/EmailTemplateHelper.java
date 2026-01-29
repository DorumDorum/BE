package com.project.dorumdorum.domain.user.infra.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailTemplateHelper {

    public static EmailContent generate(String code) {
        String subject = "[도룸도룸] 이메일 인증번호 안내";

        String body =
                """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <title>[도룸도룸] 이메일 인증번호 안내</title>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin:0;padding:0;background-color:#f4f4f5;">
                <table role="presentation" cellpadding="0" cellspacing="0" width="100%%" style="border-collapse:collapse;">
                    <tr>
                        <td align="center" style="padding:40px 16px;">
                            <table cellpadding="0" cellspacing="0" width="100%%" style="max-width:480px;border-collapse:collapse;background-color:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
                                <tr>
                                    <td style="padding:24px 24px 16px 24px;background:linear-gradient(135deg,#4f46e5,#8b5cf6);color:#ffffff;">
                                        <h1 style="margin:0;font-size:20px;font-weight:700;letter-spacing:-0.02em;">
                                            도룸도룸 이메일 인증
                                        </h1>
                                        <p style="margin:8px 0 0 0;font-size:13px;opacity:0.9;">
                                            안전한 서비스 이용을 위해 이메일을 인증해 주세요.
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding:24px 24px 8px 24px;color:#111827;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;">
                                        <p style="margin:0 0 12px 0;font-size:14px;line-height:1.6;color:#4b5563;">
                                            안녕하세요, <span style="font-weight:600;">도룸도룸</span> 입니다.
                                        </p>
                                        <p style="margin:0 0 20px 0;font-size:14px;line-height:1.6;color:#4b5563;">
                                            아래 <strong>이메일 인증번호 6자리</strong>를
                                            도룸도룸 화면에 입력해 인증을 완료해 주세요.
                                        </p>
                                        <div style="margin:0 auto 20px auto;text-align:center;">
                                            <div style="display:inline-block;padding:14px 24px;border-radius:999px;background-color:#eef2ff;border:1px solid #c7d2fe;">
                                                <span style="font-size:22px;font-weight:700;letter-spacing:0.35em;color:#1d4ed8;">
                                                    %s
                                                </span>
                                            </div>
                                        </div>
                                        <p style="margin:0 0 4px 0;font-size:13px;line-height:1.5;color:#6b7280;">
                                            · 인증번호 유효 시간: <strong>10분</strong>
                                        </p>
                                        <p style="margin:0 0 16px 0;font-size:13px;line-height:1.5;color:#6b7280;">
                                            · 타인에게 공유하지 말고, 도룸도룸 서비스 내에서만 사용해 주세요.
                                        </p>
                                        <p style="margin:0;font-size:12px;line-height:1.6;color:#9ca3af;">
                                            만약 본 메일을 요청하지 않으셨다면, 이 메일은 무시하셔도 됩니다.
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding:16px 24px 20px 24px;border-top:1px solid #e5e7eb;background-color:#f9fafb;color:#9ca3af;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;">
                                        <p style="margin:0 0 4px 0;font-size:11px;line-height:1.5;">
                                            본 메일은 발신 전용입니다. 문의는 서비스 내 고객센터를 이용해 주세요.
                                        </p>
                                        <p style="margin:0;font-size:11px;line-height:1.5;">
                                            &copy; 2026 도룸도룸. All rights reserved.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                </body>
                </html>
                """.formatted(code);

        return new EmailContent(subject, body);
    }

    public record EmailContent(
            String subject,
            String body
    ) {}
}
