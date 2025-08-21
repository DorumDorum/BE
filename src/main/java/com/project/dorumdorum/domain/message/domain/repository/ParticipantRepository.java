package com.project.dorumdorum.domain.message.domain.repository;

import com.project.dorumdorum.domain.message.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
