package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String>, ChatRoomQueryRepository {

    Optional<ChatRoom> findByRoomNoAndChatRoomType(String roomNo, ChatRoomType chatRoomType);
    List<ChatRoom> findAllByRoomNo(String roomNo);

    boolean existsByRoomNo(String roomNo);

    boolean existsByRoomNoAndChatRoomTypeAndApplicantUserNo(
            String roomNo, ChatRoomType chatRoomType, String applicantUserNo);

    Optional<ChatRoom> findByRoomNoAndChatRoomTypeAndApplicantUserNo(
            String roomNo, ChatRoomType chatRoomType, String applicantUserNo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM ChatRoom c WHERE c.chatRoomNo = :chatRoomNo")
    void deleteByChatRoomNo(@Param("chatRoomNo") String chatRoomNo);
}
