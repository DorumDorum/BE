package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("""
        select case when count(m.messageRoomNo) > 0 then true else false end
        from MessageRoom m
        where m.roomType = :roomType
          and m.roomStatus in :statuses
          and m.messageRoomNo in (
            select p1.messageRoomNo
            from Participant p1
            where p1.user.userNo = :senderId
          )
          and m.messageRoomNo in (
            select p2.messageRoomNo
            from Participant p2
            where p2.user.userNo = :receiverId
          )
        """)
    boolean existsSharedRoomByUserIdsAndRoomType(
        @Param("senderId") Long senderId,
        @Param("receiverId") Long receiverId,
        @Param("roomType") MessageRoomType roomType,
        @Param("statuses") Collection<MessageRoomStatus> statuses
    );
}
