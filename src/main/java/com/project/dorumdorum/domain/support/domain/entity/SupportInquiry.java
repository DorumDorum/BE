package com.project.dorumdorum.domain.support.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "support_inquiries")
public class SupportInquiry extends BaseEntity {

    @Id
    @Tsid
    private String inquiryNo;

    @Column(nullable = false)
    private String userNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportInquiryCategory category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportInquiryStatus status;

    @PrePersist
    public void init() {
        if (status == null) {
            status = SupportInquiryStatus.RECEIVED;
        }
    }
}
