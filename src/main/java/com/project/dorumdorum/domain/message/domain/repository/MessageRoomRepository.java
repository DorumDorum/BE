package com.project.dorumdorum.domain.message.domain.repository;

import com.project.dorumdorum.domain.message.domain.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {
}
