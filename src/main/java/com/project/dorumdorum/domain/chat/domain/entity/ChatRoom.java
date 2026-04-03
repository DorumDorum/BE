package com.project.dorumdorum.domain.chat.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_chat_room_last_message_at", columnList = "last_message_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatRoom extends BaseEntity {

    @Id @Tsid
    private String chatRoomNo;

    @Column(nullable = false)
    private String roomNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ChatRoomType chatRoomType = ChatRoomType.GROUP;

    /** DIRECT 채팅방의 지원자 userNo (GROUP이면 null) */
    private String applicantUserNo;

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
