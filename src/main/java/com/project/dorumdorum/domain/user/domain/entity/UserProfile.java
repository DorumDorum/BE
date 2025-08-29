package com.project.dorumdorum.domain.user.domain.entity;

import com.project.dorumdorum.domain.Image.domain.entity.Image;
import com.project.dorumdorum.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserProfile extends BaseEntity {
    @Id
    private Long userNo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_no")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_no")
    private Image profileImage;

    private SchoolName schoolName;

    private String majorName;

    private String studentNo;

}
