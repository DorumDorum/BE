package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;

    public void saveToken(Long userNo, String firebaseToken) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        user.updateFirebaseToken(firebaseToken);
    }
}

