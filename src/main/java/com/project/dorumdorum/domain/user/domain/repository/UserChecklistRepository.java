package com.project.dorumdorum.domain.user.domain.repository;

import com.project.dorumdorum.domain.user.domain.entity.UserChecklist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserChecklistRepository extends MongoRepository<UserChecklist, String> {

    Optional<UserChecklist> findByUserNo(String userNo);

}
