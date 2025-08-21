package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {
}
