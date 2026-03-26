package com.project.dorumdorum.domain.chat.domain.entity;

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
@Table(
    uniqueConstraints = @UniqueConstraint(
        name = "uk_chat_room_member_room_user",
        columnNames = {"chat_room_no", "user_no"}
    )
)
public class ChatRoomMember extends BaseEntity {

    @Id @Tsid
    private String chatRoomMemberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_no", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime lastReadAt;

    @PrePersist
    public void init() {
        this.joinedAt = LocalDateTime.now();
    }

    public void updateLastReadAt(LocalDateTime readAt) {
        this.lastReadAt = readAt;
    }
}
