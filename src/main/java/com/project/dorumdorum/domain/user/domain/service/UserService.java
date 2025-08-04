package com.project.dorumdorum.domain.user.domain.service;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public boolean isAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(SignUpRequest request) {
        return userRepository.save(
                User.builder()
                        .email(request.email())
                        .name(request.name())
                        .nickname(request.nickname())
                        .password(passwordEncoder.encode(request.password()))
                        .role(Role.USER)
                        .gender(request.gender())
                        .build()
        );
    }

    public User findById(Long userNo) {
        return userRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public Boolean existsById(Long userNo) {
        return userRepository.existsById(userNo);
    }
}
