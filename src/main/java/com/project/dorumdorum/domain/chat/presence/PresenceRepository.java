package com.project.dorumdorum.domain.chat.presence;

import java.util.Optional;

public interface PresenceRepository {
    void save(PresenceSnapshot snapshot, long ttlSeconds);
    Optional<PresenceSnapshot> find(String userId);
    void delete(String userId);
}
