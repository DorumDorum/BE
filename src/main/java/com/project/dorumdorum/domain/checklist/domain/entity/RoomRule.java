package com.project.dorumdorum.domain.checklist.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "room_rule")
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
