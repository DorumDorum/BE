package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateRepository extends JpaRepository<Roommate, Long> {
}
