package com.project.dorumdorum.domain.room.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Room {

    @Id @Tsid
    private Long roomNo;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    private Integer capacity;

    private Integer current_mate_count;

    private List<Tag> tags;

    private String title;

    @PrePersist
    public void init() {
        this.current_mate_count = 1;
        this.roomStatus = RoomStatus.CONFIRM_PENDING;
    }

    public void plusCurrentMate() {
        this.current_mate_count++;
    }

    public boolean isFull() {
        return this.current_mate_count.equals(capacity);
    }

    public boolean isPending() {
        return RoomStatus.CONFIRM_PENDING.equals(this.roomStatus);
    }

    public void updateStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }
}
