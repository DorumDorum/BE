package com.project.dorumdorum.domain.chat.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@SQLRestriction("deleted_at is null")
@Table(
    indexes = @Index(name = "idx_chat_message_room_created_message", columnList = "chat_room_no, created_at, message_no")
)
public class ChatMessage extends BaseEntity {

    @Id @Tsid
    private String messageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_no", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String senderNo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    @Column(nullable = false)
    private int unreadCount;
}
