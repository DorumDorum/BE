package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.repository.ParticipantRepository;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Participant> findActiveParticipantsByRoomNo(Long messageRoomNo) {
        return participantRepository.findByMessageRoomNoAndDeletedAtIsNull(messageRoomNo);
    }
}
