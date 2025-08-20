package com.project.dorumdorum.domain.message.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MessageRequest extends BaseEntity {

    @Id @Tsid
    private Long messageRequestNo;

    @Column(nullable = false)
    private Long userNo;

    @Column(nullable = false)
    private Long roomNo;
}
