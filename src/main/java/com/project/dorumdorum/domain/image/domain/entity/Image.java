package com.project.dorumdorum.domain.image.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Image extends BaseEntity {

    @Id
    @Tsid
    private Long imageNo;

    @Column(nullable = false)
    private Long noticeNo;

    @Column(nullable = false)
    private String s3Key;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long fileSize;
}

