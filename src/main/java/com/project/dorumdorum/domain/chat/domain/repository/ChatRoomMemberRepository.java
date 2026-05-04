package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, String> {

    Optional<ChatRoomMember> findByChatRoomAndUserNo(ChatRoom chatRoom, String userNo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select member
            from ChatRoomMember member
            where member.chatRoom.chatRoomNo = :chatRoomNo
              and member.userNo = :userNo
            """)
    Optional<ChatRoomMember> findByChatRoomNoAndUserNoForUpdate(
            @Param("chatRoomNo") String chatRoomNo,
            @Param("userNo") String userNo
    );

    boolean existsByChatRoomAndUserNo(ChatRoom chatRoom, String userNo);

    List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom);

    long countByChatRoom(ChatRoom chatRoom);

    boolean existsByChatRoom_ChatRoomNoAndUserNo(String chatRoomNo, String userNo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM ChatRoomMember m WHERE m.chatRoom = :chatRoom")
    void deleteAllByChatRoom(@Param("chatRoom") ChatRoom chatRoom);
}
