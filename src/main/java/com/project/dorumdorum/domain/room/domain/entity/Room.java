package com.project.dorumdorum.domain.room.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Room extends BaseEntity {

    @Id @Tsid
    private Long roomNo;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    private Integer capacity;

    private Integer currentMateCount;

    private Integer confirmMateCount;

    private List<Tag> tags;

    private String title;

    @PrePersist
    public void init() {
        this.currentMateCount = 1;
        this.confirmMateCount = 0;
        this.roomStatus = RoomStatus.CONFIRM_PENDING;
    }

    public void plusCurrentMate() {
        this.currentMateCount++;
    }

    public void plusConfirmMate() {
        this.confirmMateCount++;
    }

    public void minusCurrentMate() {
        this.currentMateCount--;
    }

    public void clearConfirmMate() {
        this.confirmMateCount = 0;
    }

    public boolean isFull() {
        return this.currentMateCount.equals(capacity);
    }

    public boolean isPending() {
        return RoomStatus.CONFIRM_PENDING.equals(this.roomStatus);
    }

    public void updateStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }
}
