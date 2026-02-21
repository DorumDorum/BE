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

    private LocalDateTime lastReadSentAt;

    public boolean updateLastRead(String lastReadMessageId, LocalDateTime lastReadSentAt) {
        // 입력값 검증
        if (lastReadMessageId == null || lastReadSentAt == null) {
            return false;
        }

        // 기존 읽음 메시지가 없으면 업데이트
        if (this.lastReadSentAt == null) {
            this.lastReadMessageId = lastReadMessageId;
            this.lastReadSentAt = lastReadSentAt;
            return true;
        }

        // 이전 읽은 시간보다 최신이면 업데이트
        int sentAtCompare = lastReadSentAt.compareTo(this.lastReadSentAt);
        if (sentAtCompare > 0) {
            this.lastReadMessageId = lastReadMessageId;
            this.lastReadSentAt = lastReadSentAt;
            return true;
        }

        // 이전 읽은 시간과 같으면 메시지 ID 비교
        if (sentAtCompare == 0 && (this.lastReadMessageId == null
                || lastReadMessageId.compareTo(this.lastReadMessageId) > 0)) {
            this.lastReadMessageId = lastReadMessageId;
            this.lastReadSentAt = lastReadSentAt;
            return true;
        }

        return false;
    }

    public void softDelete() {
        this.leftAt = LocalDateTime.now();
        delete();
    }
}
