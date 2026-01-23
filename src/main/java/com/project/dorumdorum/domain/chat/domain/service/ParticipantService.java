package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.repository.ParticipantRepository;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
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

    public void softDelete(Participant participant) {
        participant.softDelete();
    }

    public Participant findByUserNoAndMessageRoomNo(User user, Long messageRoomNo) {
        Participant participant = participantRepository.findByUserAndMessageRoomNo(user, messageRoomNo);
        if (participant == null) {
            throw new RestApiException(GlobalErrorStatus.PARTICIPANT_NOT_FOUND);
        }
        return participant;
    }
}
