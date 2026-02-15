package com.project.dorumdorum.domain.user.fixture;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.domain.entity.Gender;

public class RequestFixture {

    public static SignUpRequest createValidSignUpRequest() {
        return new SignUpRequest(
                "홍길동",
                "테스터",
                "test@university.ac.kr",
                "password123!",
                "password123!",
                Gender.MALE,
                "20210001",
                "컴퓨터공학과",
                "3",
                "2000-01-01"
        );
    }

    public static SignUpRequest createSignUpRequestWithEmail(String email) {
        return new SignUpRequest(
                "김철수",
                "테스터2",
                email,
                "password123!",
                "password123!",
                Gender.MALE,
                "20210002",
                "전자공학과",
                "2",
                "2001-05-15"
        );
    }

    public static SignUpRequest createSignUpRequestWithMismatchedPassword() {
        return new SignUpRequest(
                "이영희",
                "테스터3",
                "mismatch@university.ac.kr",
                "password123!",
                "differentPassword!",
                Gender.FEMALE,
                "20210003",
                "경영학과",
                "4",
                "1999-12-25"
        );
    }

    public static SignUpRequest createSignUpRequestWithInvalidBirth() {
        return new SignUpRequest(
                "박민수",
                "테스터4",
                "invalid@university.ac.kr",
                "password123!",
                "password123!",
                Gender.MALE,
                "20210004",
                "물리학과",
                "1",
                "invalid-date"
        );
    }

    public static LoginRequest createValidLoginRequest() {
        return new LoginRequest(
                "test@university.ac.kr",
                "password123!"
        );
    }

    public static LoginRequest createLoginRequestWithEmail(String email) {
        return new LoginRequest(
                email,
                "password123!"
        );
    }

    public static LoginRequest createLoginRequestWithWrongPassword() {
        return new LoginRequest(
                "test@university.ac.kr",
                "wrongPassword!"
        );
    }

    public static LoginRequest createLoginRequestWithNonExistentEmail() {
        return new LoginRequest(
                "nonexistent@university.ac.kr",
                "password123!"
        );
    }
}
