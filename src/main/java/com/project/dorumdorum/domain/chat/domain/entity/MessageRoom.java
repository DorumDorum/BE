package com.project.dorumdorum.domain.chat.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MessageRoom extends BaseEntity {

    @Id @Tsid
    private String messageRoomNo;

    @Enumerated(EnumType.STRING)
    private MessageRoomType roomType;

    @Column(length = 64)
    private String directRoomKey;

    @Column(unique = true, length = 64)
    private String activeDirectRoomKey;

    private String lastMessage;

    private LocalDateTime lastMessageAt;

    @Enumerated(EnumType.STRING)
    private MessageRoomStatus roomStatus;

    private String roomNo;

    public void approve() {
        this.roomStatus = MessageRoomStatus.APPROVED;
    }

    public void reject() {
        this.roomStatus = MessageRoomStatus.REJECTED;
        this.activeDirectRoomKey = null;
    }

    public void updateLastMessage(String lastMessage, LocalDateTime lastMessageAt) {
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
    }

    public void delete() {
        this.roomStatus = MessageRoomStatus.DELETED;
        this.activeDirectRoomKey = null;
        super.delete(); 
    }
}
