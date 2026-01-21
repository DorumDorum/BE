package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.repository.ParticipantRepository;
import com.project.dorumdorum.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public void create(User user, Long messageRoomNo) {
        Participant participant = Participant.builder()
            .user(user)
            .messageRoomNo(messageRoomNo)
            .joinedAt(LocalDateTime.now())
            .build();

        participantRepository.save(participant);
    }

    public boolean existsDirectMessageRoomByUserIds(Long senderId, Long receiverId) {
        return participantRepository.existsSharedRoomByUserIdsAndRoomType(
            senderId,
            receiverId,
            MessageRoomType.DIRECT,
            EnumSet.of(MessageRoomStatus.REQUESTED, MessageRoomStatus.APPROVED)
        );
    }
}
