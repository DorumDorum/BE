package com.project.dorumdorum.domain.user.domain.service;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus.DUPLICATE_SIGN_UP_INFO;
import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus.EMAIL_NOT_FOUND;
import static com.project.dorumdorum.global.exception.code.status.CommonErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(EMAIL_NOT_FOUND));
    }

    public boolean isAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isAlreadyRegisteredStudentNo(String studentNo) {
        return userRepository.existsByStudentNo(studentNo);
    }

    public User save(SignUpRequest request) {
        try {
            return userRepository.save(
                    User.builder()
                            .email(request.email())
                            .name(request.name())
                            .nickname(request.nickname())
                            .password(passwordEncoder.encode(request.password()))
                            .role(Role.USER)
                            .gender(request.gender())
                            .studentNo(request.studentNo())
                            .major(request.major())
                            .grade(request.grade())
                            .birth(request.birth())
                            .age(request.calculateAge())
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            throw new RestApiException(DUPLICATE_SIGN_UP_INFO);
        }
    }

    public User findById(String userNo) {
        return userRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public void validateExistsById(String userNo) {
        if (!userRepository.existsById(userNo))
            throw new RestApiException(_NOT_FOUND);
    }
}
