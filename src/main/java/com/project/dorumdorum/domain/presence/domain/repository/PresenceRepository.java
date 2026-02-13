package com.project.dorumdorum.domain.presence.domain.repository;

import com.project.dorumdorum.domain.presence.domain.entity.PresenceSnapshot;

import java.util.Optional;

public interface PresenceRepository {
    void save(PresenceSnapshot snapshot, long ttlSeconds);
    Optional<PresenceSnapshot> find(String userId);
    void delete(String userId);
}
