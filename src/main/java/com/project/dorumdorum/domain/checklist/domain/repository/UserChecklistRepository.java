package com.project.dorumdorum.domain.checklist.domain.repository;

import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChecklistRepository extends JpaRepository<UserChecklist, String> {

    Optional<UserChecklist> findByUserNo(String userNo);

    boolean existsByUserNo(String userNo);
}
