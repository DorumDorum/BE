package com.project.dorumdorum.domain.room.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Room extends BaseEntity {

    @Id @Tsid
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roomNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus roomStatus;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer currentMateCount;

    @Column(nullable = false)
    private Integer remaining;

    @Column(nullable = false)
    private Integer confirmMateCount;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long hostUserNo;

    @PrePersist
    public void init() {
        this.currentMateCount = 1;
        this.confirmMateCount = 0;
        this.remaining = capacity - currentMateCount;
        this.roomStatus = RoomStatus.CONFIRM_PENDING;
    }

    public void plusCurrentMate() {
        this.currentMateCount++;
        this.remaining = capacity - currentMateCount;
    }

    public void plusConfirmMate() {
        this.confirmMateCount++;
    }

    public void minusCurrentMate() {
        this.currentMateCount--;
        this.remaining = capacity - currentMateCount;
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

    public boolean isHost(Long userNo) {
        return this.hostUserNo.equals(userNo);
    }
}
