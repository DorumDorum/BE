package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, String> {

    Participant findByUserAndMessageRoomNo(User user, String messageRoomNo);
    Participant findByUser_UserNoAndMessageRoomNo(String userNo, String messageRoomNo);
    List<Participant> findByMessageRoomNoAndDeletedAtIsNull(String messageRoomNo);
    List<Participant> findByMessageRoomNoAndDeletedAtIsNullOrderByJoinedAtAsc(String messageRoomNo);
}
