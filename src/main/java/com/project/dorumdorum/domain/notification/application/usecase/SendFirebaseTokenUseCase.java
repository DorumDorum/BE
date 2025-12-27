package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.application.dto.request.SendFirebaseTokenRequest;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SendFirebaseTokenUseCase {

    private final NotificationService notificationService;

    public void execute(Long userNo, SendFirebaseTokenRequest request) {
        notificationService.saveToken(userNo, request.firebaseToken());
    }
}

