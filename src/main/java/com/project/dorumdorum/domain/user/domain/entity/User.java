package com.project.dorumdorum.domain.user.domain.entity;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id @Tsid
    private String userNo;

    private String nickname;

    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String studentNo;

    private String major;

    private String grade;

    private String birth;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String firebaseToken;

    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        this.nickname = updateProfileRequest.nickname();
        this.grade = updateProfileRequest.grade();
        this.major = updateProfileRequest.major();
    }

    public void updateFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

}
