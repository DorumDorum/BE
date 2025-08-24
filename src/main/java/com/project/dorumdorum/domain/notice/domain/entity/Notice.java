package com.project.dorumdorum.domain.notice.domain.entity;

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
public class Notice extends BaseEntity {
    @Id @Tsid
    private Long noticeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_no", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Long userNo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
}
