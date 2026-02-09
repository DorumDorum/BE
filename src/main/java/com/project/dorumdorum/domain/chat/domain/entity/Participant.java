package com.project.dorumdorum.domain.chat.domain.entity;

import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Participant extends BaseEntity {

    @Id @Tsid
    private String participantNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    private String messageRoomNo;

    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    private String lastReadMessageId;

    public void softDelete() {
        this.leftAt = LocalDateTime.now();
        delete();
    }
}
