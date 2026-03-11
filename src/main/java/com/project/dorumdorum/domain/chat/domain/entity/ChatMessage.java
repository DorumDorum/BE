package com.project.dorumdorum.domain.chat.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity {

    @Id
    @Tsid
    private String messageNo;

    @Column(nullable = false)
    private String senderNo;

    @Column(nullable = false)
    private String recipientNo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
