package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String>, ChatMessageQueryRepository {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE ChatMessage m SET m.unreadCount = m.unreadCount - 1 " +
           "WHERE m.chatRoom.chatRoomNo = :chatRoomNo " +
           "AND m.createdAt > :fromTime " +
           "AND m.senderNo != :userNo " +
           "AND m.unreadCount > 0")
    int decreaseUnreadCount(@Param("chatRoomNo") String chatRoomNo,
                            @Param("fromTime") LocalDateTime fromTime,
                            @Param("userNo") String userNo);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ChatMessage m WHERE m.chatRoom.chatRoomNo = :chatRoomNo")
    void deleteByChatRoomNo(@Param("chatRoomNo") String chatRoomNo);
}
