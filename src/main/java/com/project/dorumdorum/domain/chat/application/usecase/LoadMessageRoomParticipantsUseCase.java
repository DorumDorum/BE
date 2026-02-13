package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessageRoomParticipantResponse;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMessageRoomParticipantsUseCase {

    private final UserService userService;
    private final MessageRoomService messageRoomService;
    private final ParticipantService participantService;
    private final RoomService roomService;

    @Transactional(readOnly = true)
    public List<LoadMessageRoomParticipantResponse> execute(String userNo, String messageRoomNo) {
        // 사용자 및 채팅방 검증
        User requester = userService.findById(userNo);
        MessageRoom messageRoom = messageRoomService.findById(messageRoomNo);

        // 채팅방 삭제 검증
        if (messageRoom.getRoomStatus() == MessageRoomStatus.DELETED) {
            throw new RestApiException(GlobalErrorStatus.MESSAGEROOM_NOT_FOUND);
        }

        // 참여자 검증
        Participant requesterParticipant = participantService.findByUserNoAndMessageRoomNo(requester, messageRoomNo);
        if (requesterParticipant.getLeftAt() != null) {
            throw new RestApiException(GlobalErrorStatus._FORBIDDEN);
        }

        // 방이 확정 상태인지 확인
        boolean isRoomCompleted = isRoomCompleted(messageRoom.getRoomNo());

        // 참여자 조회
        return participantService.findActiveParticipantsByRoomNo(messageRoomNo).stream()
            .map(participant -> toResponse(participant.getUser(), isRoomCompleted))
            .toList();
    }

    private boolean isRoomCompleted(String roomNo) {
        if (roomNo == null || roomNo.isBlank()) {
            return false;
        }

        return roomService.findById(roomNo).getRoomStatus() == RoomStatus.COMPLETED;
    }

    // 참여자 정보 응답 생성 (방이 확정 상태면 실명/학번/전공을 노출)
    private LoadMessageRoomParticipantResponse toResponse(User user, boolean isRoomCompleted) {
        return LoadMessageRoomParticipantResponse.builder()
            .profileImageUrl(null)
            .userId(user.getUserNo())
            .name(isRoomCompleted ? user.getName() : user.getNickname())
            .studentNo(isRoomCompleted ? user.getStudentNo() : null)
            .major(user.getMajor())
            .age(user.getAge())
            .build();
    }
}
