package com.project.dorumdorum.domain.user.unit.entity;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Entity Tests")
class UserTest {

    @Test
    @DisplayName("Should create user with builder pattern")
    void builder_WithAllFields_CreatesUser() {
        // Act
        User user = User.builder()
                .userNo("0000000000000001")
                .name("홍길동")
                .nickname("테스터")
                .email("test@university.ac.kr")
                .password("encodedPassword")
                .role(Role.USER)
                .studentNo("20210001")
                .major("컴퓨터공학과")
                .grade("3")
                .birth("2000-01-01")
                .age(25)
                .gender(Gender.MALE)
                .firebaseToken("test-token")
                .build();

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getUserNo()).isEqualTo("0000000000000001");
        assertThat(user.getName()).isEqualTo("홍길동");
        assertThat(user.getNickname()).isEqualTo("테스터");
        assertThat(user.getEmail()).isEqualTo("test@university.ac.kr");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getAge()).isEqualTo(25);
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    @DisplayName("Should update profile with UpdateProfileRequest")
    void updateProfile_WithValidRequest_UpdatesFields() {
        // Arrange
        User user = UserFixture.createDefaultUser();
        UpdateProfileRequest request = new UpdateProfileRequest(
                "새로운닉네임",
                "4",
                "전자공학과"
        );

        String originalEmail = user.getEmail();
        String originalName = user.getName();

        // Act
        user.updateProfile(request);

        // Assert
        assertThat(user.getNickname()).isEqualTo("새로운닉네임");
        assertThat(user.getGrade()).isEqualTo("4");
        assertThat(user.getMajor()).isEqualTo("전자공학과");
        // 변경되지 않아야 하는 필드들
        assertThat(user.getEmail()).isEqualTo(originalEmail);
        assertThat(user.getName()).isEqualTo(originalName);
    }

    @Test
    @DisplayName("Should update firebase token")
    void updateFirebaseToken_WithNewToken_UpdatesToken() {
        // Arrange
        User user = UserFixture.createDefaultUser();
        String newToken = "new-firebase-token-123";

        // Act
        user.updateFirebaseToken(newToken);

        // Assert
        assertThat(user.getFirebaseToken()).isEqualTo(newToken);
    }

    @Test
    @DisplayName("Should allow null firebase token")
    void updateFirebaseToken_WithNull_SetsTokenToNull() {
        // Arrange
        User user = UserFixture.createDefaultUser();

        // Act
        user.updateFirebaseToken(null);

        // Assert
        assertThat(user.getFirebaseToken()).isNull();
    }

    @Test
    @DisplayName("Should preserve other fields when updating profile")
    void updateProfile_PreservesOtherFields() {
        // Arrange
        User user = UserFixture.createDefaultUser();
        UpdateProfileRequest request = new UpdateProfileRequest(
                "업데이트닉네임",
                "2",
                "경영학과"
        );

        String originalUserNo = user.getUserNo();
        String originalEmail = user.getEmail();
        String originalPassword = user.getPassword();
        Role originalRole = user.getRole();
        Gender originalGender = user.getGender();

        // Act
        user.updateProfile(request);

        // Assert
        assertThat(user.getUserNo()).isEqualTo(originalUserNo);
        assertThat(user.getEmail()).isEqualTo(originalEmail);
        assertThat(user.getPassword()).isEqualTo(originalPassword);
        assertThat(user.getRole()).isEqualTo(originalRole);
        assertThat(user.getGender()).isEqualTo(originalGender);
    }
}
