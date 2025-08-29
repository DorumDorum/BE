package com.project.dorumdorum.domain.image.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Image extends BaseEntity {

    @Id @Tsid
    private Long imageNo;

    private String imageKey; // 확장자도 뒤에 포함 s3할 때 String fileKey = String.format("images/%s/%s.%s",
                // user.getUserId(), java.util.UUID.randomUUID(), extension);

    private String originalImageName;

    private String storedImageName;
}