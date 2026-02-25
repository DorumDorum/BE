package com.project.dorumdorum.global.warmer;

import com.project.dorumdorum.domain.chat.domain.repository.MessageRepository;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.domain.user.domain.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarmupService {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final MessageRepository messageRepository;
    private final JavaMailSender mailSender;

    @Transactional(readOnly = true)
    public void warm() {
        log.info("[Warmup] Starting warmup sequence");

        warmRdb();
        warmRedis();
        warmMongo();
        warmEmail();

        log.info("[Warmup] Warmup sequence completed");
    }

    private void warmRdb() {
        try {
            long userCount = userRepository.count();
            log.info("[Warmup] RDB/UserRepository.count executed, count={}", userCount);
        } catch (Exception e) {
            log.warn("[Warmup] RDB warmup failed", e);
        }
    }

    private void warmRedis() {
        try {
            String dummyUserNo = "WARMUP_DUMMY";
            String dummyToken = "WARMUP_TOKEN";
            refreshTokenService.saveRefreshToken(dummyUserNo, dummyToken, java.time.Duration.ofSeconds(5));
            boolean exists = refreshTokenService.isExist(dummyToken, dummyUserNo);
            refreshTokenService.deleteRefreshToken(dummyUserNo);
            log.info("[Warmup] Redis/RefreshTokenService executed, exists={}", exists);
        } catch (Exception e) {
            log.warn("[Warmup] Redis warmup failed", e);
        }
    }

    private void warmMongo() {
        try {
            messageRepository.findAll(PageRequest.of(0, 1));
            log.info("[Warmup] Mongo/MessageRepository.findAll(PageRequest) executed");
        } catch (Exception e) {
            log.warn("[Warmup] Mongo warmup failed", e);
        }
    }

    private void warmEmail() {
        try {
            if (mailSender instanceof JavaMailSenderImpl impl) {
                impl.testConnection();
                log.info("[Warmup] Email/JavaMailSenderImpl.testConnection executed");
            } else {
                log.info("[Warmup] Email warmup skipped - mailSender is not JavaMailSenderImpl");
            }
        } catch (Exception e) {
            log.warn("[Warmup] Email warmup failed", e);
        }
    }
}

