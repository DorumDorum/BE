package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, String>, MessageRoomRepositoryCustom {
    boolean existsByActiveDirectRoomKeyAndRoomTypeAndRoomStatusIn(
        String activeDirectRoomKey,
        MessageRoomType roomType,
        Collection<MessageRoomStatus> statuses
    );
}
