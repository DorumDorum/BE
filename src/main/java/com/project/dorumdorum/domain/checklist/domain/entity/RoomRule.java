package com.project.dorumdorum.domain.checklist.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "room_rule",
        indexes = {
                @Index(name = "idx_room_rule_bedtime", columnList = "bedtime"),
                @Index(name = "idx_room_rule_wake_up", columnList = "wake_up"),
                @Index(name = "idx_room_rule_return_home", columnList = "return_home"),
                @Index(name = "idx_room_rule_return_home_time", columnList = "return_home_time"),
                @Index(name = "idx_room_rule_cleaning", columnList = "cleaning"),
                @Index(name = "idx_room_rule_phone_call", columnList = "phone_call"),
                @Index(name = "idx_room_rule_sleep_light", columnList = "sleep_light"),
                @Index(name = "idx_room_rule_sleep_habit", columnList = "sleep_habit"),
                @Index(name = "idx_room_rule_snoring", columnList = "snoring"),
                @Index(name = "idx_room_rule_shower_time", columnList = "shower_time"),
                @Index(name = "idx_room_rule_eating", columnList = "eating"),
                @Index(name = "idx_room_rule_lights_out", columnList = "lights_out"),
                @Index(name = "idx_room_rule_lights_out_time", columnList = "lights_out_time"),
                @Index(name = "idx_room_rule_home_visit", columnList = "home_visit"),
                @Index(name = "idx_room_rule_smoking", columnList = "smoking"),
                @Index(name = "idx_room_rule_refrigerator", columnList = "refrigerator"),
                @Index(name = "idx_room_rule_updated_at", columnList = "updated_at"),
                @Index(name = "uk_room_rule_room_no", columnList = "room_no", unique = true)
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class RoomRule extends ChecklistBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_rule_no", updatable = false, nullable = false)
    private String roomRuleNo;

    @Column(name = "room_no", nullable = false, unique = true)
    private String roomNo;
}
