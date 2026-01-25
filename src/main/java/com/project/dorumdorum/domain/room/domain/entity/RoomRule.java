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
public class RoomRule extends BaseEntity {

    @Id @Tsid
    private Long ruleNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_no", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "roomRule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RuleItem> items = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String otherNotes; // 기타 메모 사항

    public void addItem(RuleItem item) {
        this.items.add(item);
        item.setRoomRule(this);
    }

    public void updateOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }
}
