package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRequestRepository extends JpaRepository<MessageRequest, Long> {
}
