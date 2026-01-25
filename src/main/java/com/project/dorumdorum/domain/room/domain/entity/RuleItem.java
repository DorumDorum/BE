package com.project.dorumdorum.domain.room.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RuleItem extends BaseEntity {

    @Id @Tsid
    private Long itemNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_no", nullable = false)
    @Setter
    private RoomRule roomRule;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RuleItemCategory category; // 기본 정보, 생활 패턴, 추가 규칙

    @Column(nullable = false)
    private String label; // 취침, 기상, 거주기간, 귀가 등

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleItemType itemType; // VALUE, OPTION

    @Column
    private String value; // itemType이 VALUE일 때 사용 (예: "12-1", "7-9")

    @Column
    private String extraValue; // 추가 값 (예: 소등 시간의 구체적인 시간)

    @OneToMany(mappedBy = "ruleItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RuleOption> options = new ArrayList<>();

    public void addOption(RuleOption option) {
        this.options.add(option);
        option.setRuleItem(this);
    }

    public void updateValue(String value) {
        this.value = value;
    }

    public void updateExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }
}
