package com.project.dorumdorum.domain.chat.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
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
    private Long messageRoomNo;

    @Enumerated(EnumType.STRING)
    private MessageRoomType roomType;

    private String lastMessage;

    private LocalDateTime lastMessageAt;

    @Enumerated(EnumType.STRING)
    private MessageRoomStatus roomStatus;
}
