package com.project.dorumdorum.domain.roommate.domain.entity;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(
        indexes = {
                @Index(name = "idx_roommate_room_no", columnList = "room_no"),
                @Index(name = "idx_roommate_user_no", columnList = "user_no"),
                @Index(name = "uk_roommate_user_room", columnList = "user_no, room_no", unique = true)
        }
)
public class Roommate extends BaseEntity {

    @Id @Tsid
    private String roommateNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_no", nullable = false)
    private Room room;

    @Column(nullable = false)
    private String userNo;

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

    public void accept() {
        this.confirmStatus = ConfirmStatus.ACCEPTED;
    }

    public void complete() {
        this.confirmStatus = ConfirmStatus.COMPLETED;
    }
}
