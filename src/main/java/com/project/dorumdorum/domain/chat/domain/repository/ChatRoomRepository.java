package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String>, ChatRoomQueryRepository {

    Optional<ChatRoom> findByRoomNo(String roomNo);

    boolean existsByRoomNo(String roomNo);
}
