package com.project.dorumdorum.domain.calendar.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "calendar_events")
public class CalendarEvent extends BaseEntity {

    @Id
    @Tsid
    private String eventNo;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private LocalTime eventTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalendarEventType eventType;
}
