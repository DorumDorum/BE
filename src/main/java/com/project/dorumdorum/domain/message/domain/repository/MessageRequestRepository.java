package com.project.dorumdorum.domain.message.domain.repository;

import com.project.dorumdorum.domain.message.domain.entity.MessageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRequestRepository extends JpaRepository<MessageRequest, Long> {
}
