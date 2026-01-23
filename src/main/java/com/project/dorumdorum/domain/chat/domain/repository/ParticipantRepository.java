package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByUserAndMessageRoomNo(User user, Long messageRoomNo);
}
