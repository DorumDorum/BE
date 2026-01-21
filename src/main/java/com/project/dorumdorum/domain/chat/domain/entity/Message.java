package com.project.dorumdorum.domain.chat.domain.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@Document
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @Field(name = "message_no")
    private Long messageNo;

    @Field(name = "message_room_no")
    private Long messageRoomNo;

    @Field(name = "sender_no")
    private Long senderNo;

    @Field(name = "content")
    private String content;

    @Field(name = "message_type")
    private MessageType messageType;

    @Field(name = "attachment")
    private Attachment attachment;

    @Field(name = "is_read")
    private Boolean isRead;

    @Field("sent_at")
    private LocalDateTime sentAt;
}

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Attachment {
    private String url;
    private String name;
    private long size;
    private String mimeType;
}

