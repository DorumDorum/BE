package com.project.dorumdorum.domain.notice.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Notice extends BaseEntity {
    @Id @Tsid
    private Long noticeNo;

    @Column(nullable = false)
    private Long roomNo;

    @Column(nullable = false)
    private Long userNo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public void update(@NotBlank String title, @NotBlank String content) {
        this.title = title;
        this.content = content;
    }

    public boolean isWriter(Long userNo) {
        return this.userNo.equals(userNo);
    }
}
