package com.project.dorumdorum.domain.calendar.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "calendar_events")
public class CalendarEvent extends BaseEntity {

    @Id
    @Tsid
    private Long eventNo;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private String title;
}
