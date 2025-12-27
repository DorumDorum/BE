package com.project.dorumdorum.domain.notice.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Comment extends BaseEntity {

    @Id @Tsid
    private Long commentNo;

    @Column(nullable = false)
    private Long userNo;

    @Column(nullable = false)
    private Long noticeNo;

    @Column(nullable = false)
    private String content;

    public boolean isWriter(Long userNo) {
        return this.userNo.equals(userNo);
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
