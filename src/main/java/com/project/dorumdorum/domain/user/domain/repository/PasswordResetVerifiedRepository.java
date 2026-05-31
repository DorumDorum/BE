package com.project.dorumdorum.domain.user.domain.repository;

public interface PasswordResetVerifiedRepository {

    void save(String email);
    boolean existsByEmail(String email);
    void delete(String email);
}
