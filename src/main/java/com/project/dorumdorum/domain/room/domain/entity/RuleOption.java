package com.project.dorumdorum.domain.room.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RuleOption extends BaseEntity {

    @Id @Tsid
    private Long optionNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_no", nullable = false)
    @Setter
    private RuleItem ruleItem;

    @Column(nullable = false)
    private String text; // 옵션 텍스트 (예: "학기(16주)", "반기(24주)")

    @Column(nullable = false)
    private Boolean selected; // 선택 여부

    @Column(nullable = false)
    private Integer displayOrder; // 옵션 표시 순서

    public void updateSelected(Boolean selected) {
        this.selected = selected;
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
