package com.project.dorumdorum.domain.chat.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MessageRequest extends BaseEntity {

    @Id @Tsid
    private String messageRequestNo;

    private String senderNo;

    private String receiverNo;

    private String messageRoomNo;

    @Enumerated(EnumType.STRING)
    private MessageRequestStatus status;

    public void approve() {
        this.status = MessageRequestStatus.APPROVED;
    }

    public void reject() {
        this.status = MessageRequestStatus.REJECTED;
    }
}
