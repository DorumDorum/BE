package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String>, ChatRoomQueryRepository {

    Optional<ChatRoom> findByRoomNoAndChatRoomType(String roomNo, ChatRoomType chatRoomType);

    boolean existsByRoomNo(String roomNo);

    boolean existsByRoomNoAndChatRoomTypeAndApplicantUserNo(
            String roomNo, ChatRoomType chatRoomType, String applicantUserNo);

    Optional<ChatRoom> findByRoomNoAndChatRoomTypeAndApplicantUserNo(
            String roomNo, ChatRoomType chatRoomType, String applicantUserNo);
}
