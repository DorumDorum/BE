package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByUserAndMessageRoomNo(User user, Long messageRoomNo);
    List<Participant> findByMessageRoomNoAndDeletedAtIsNull(Long messageRoomNo);
}
