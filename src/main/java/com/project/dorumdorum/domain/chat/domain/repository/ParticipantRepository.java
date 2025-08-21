package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
