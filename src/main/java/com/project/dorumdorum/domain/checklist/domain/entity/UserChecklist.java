package com.project.dorumdorum.domain.checklist.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_checklist")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class UserChecklist extends ChecklistBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_checklist_no", updatable = false, nullable = false)
    private String userChecklistNo;

    @Column(name = "user_no", nullable = false, unique = true)
    private String userNo;
}
