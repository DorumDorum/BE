package com.project.dorumdorum.domain.chat.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatRoom extends BaseEntity {

    @Id @Tsid
    private String chatRoomNo;

    @Column(nullable = false, unique = true)
    private String roomNo;

    @Column(length = 200)
    private String lastMessageContent;

    private LocalDateTime lastMessageAt;

    private String lastSenderNo;

    public void updateLastMessage(String content, String senderNo, LocalDateTime sentAt) {
        if (this.lastMessageAt == null || sentAt.isAfter(this.lastMessageAt)) {
            this.lastMessageContent = content;
            this.lastSenderNo = senderNo;
            this.lastMessageAt = sentAt;
        }
    }
}
