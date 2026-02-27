package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.repository.ParticipantRepository;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public void create(User user, String messageRoomNo) {
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

    public Participant findByUserNoAndMessageRoomNo(User user, String messageRoomNo) {
        Participant participant = participantRepository.findByUserAndMessageRoomNo(user, messageRoomNo);
        if (participant == null) {
            throw new RestApiException(GlobalErrorStatus.PARTICIPANT_NOT_FOUND);
        }
        return participant;
    }

    @Transactional(readOnly = true)
    public List<Participant> findActiveParticipantsByRoomNo(String messageRoomNo) {
        return participantRepository.findByMessageRoomNoAndDeletedAtIsNullOrderByJoinedAtAsc(messageRoomNo);
    }

    public boolean isParticipantInMessageRoom(User user, String messageRoomNo) {
        Participant participant = participantRepository.findByUserAndMessageRoomNo(user, messageRoomNo);
        return participant != null && participant.getDeletedAt() == null;
    }

    public boolean existsByUserNoAndMessageRoomNo(User sender, String roomId) {
        return participantRepository.existsByUserAndMessageRoomNo(sender, roomId);
    }

    @Transactional
    public boolean updateLastRead(String userId, String messageRoomNo, String lastReadMessageId, LocalDateTime lastReadSentAt) {
        // 입력값 검증
        if (lastReadMessageId == null || lastReadSentAt == null) {
            return false;
        }
        // 참여자 검증
        Participant participant = participantRepository.findByUser_UserNoAndMessageRoomNo(userId, messageRoomNo);
        if (participant == null) {
            throw new RestApiException(GlobalErrorStatus.PARTICIPANT_NOT_FOUND);
        }
        // 참여자 삭제 검증
        if (participant.getDeletedAt() != null || participant.getLeftAt() != null) {
            return false;
        }

        return participant.updateLastRead(lastReadMessageId, lastReadSentAt);
    }
}
