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

    private Integer capacity;

    private Integer current_mate_count;

    private List<Tag> tags;

    private String title;

    @PrePersist
    public void init() {
        this.current_mate_count = 1;
    }
}
