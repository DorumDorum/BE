package com.project.dorumdorum.domain.user.domain.repository;

import java.util.Optional;

public interface PasswordResetCodeRepository {

    void save(String email, String code);
    Optional<String> findByEmail(String email);
    void delete(String email);
}
