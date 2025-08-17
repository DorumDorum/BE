package com.project.dorumdorum.domain.room.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Roommate {

    @Id @Tsid
    private Long roommateNo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "room_no", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Long userNo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfirmStatus confirmStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomRole roomRole;

    @PrePersist
    public void init() {
        this.confirmStatus = ConfirmStatus.PENDING;
    }

    public Boolean isCompleted() {
        return this.confirmStatus.equals(ConfirmStatus.COMPLETED);
    }

    public void cancelConfirm() {
        this.confirmStatus = ConfirmStatus.PENDING;
    }

    public void approve() {
        this.confirmStatus = ConfirmStatus.ACCEPTED;
    }
}
