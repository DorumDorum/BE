package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.request.LoadMessagesRequest;
import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessagesResponse;
import com.project.dorumdorum.domain.chat.domain.entity.Message;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.MessageService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoadMessagesUseCase {

    private final MessageService messageService;
    private final MessageRoomService messageRoomService;
    private final ParticipantService participantService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public LoadMessagesResponse execute(Long userId, Long messageRoomNo, LoadMessagesRequest request) {
        // 사용자 및 채팅방 검증
        User user = userService.findById(userId);
        MessageRoom messageRoom = messageRoomService.findById(messageRoomNo);
        
        if (messageRoom.getRoomStatus() != MessageRoomStatus.APPROVED) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }

        // 참여자 검증
        Participant participant = participantService.findByUserNoAndMessageRoomNo(user, messageRoomNo);
        if (participant.getLeftAt() != null) {
            throw new RestApiException(GlobalErrorStatus._FORBIDDEN);
        }

        // 메시지 조회 (size + 1개 조회하여 hasMore 판단)
        int pageSize = request.getPageSize();
        List<Message> messages = messageService.findMessagesByCursor(
                messageRoomNo, 
                request.cursor(), 
                pageSize + 1
        );

        // hasMore 판단
        boolean hasMore = messages.size() > pageSize;
        if (hasMore) {
            messages = messages.subList(0, pageSize);
        }

        // 발신자 정보 조회 (닉네임)
        List<Long> senderNos = messages.stream()
                .map(Message::getSenderNo)
                .distinct()
                .toList();
        
        Map<Long, String> senderNicknameMap = new HashMap<>();
        for (Long senderNo : senderNos) {
            User sender = userService.findById(senderNo);
            senderNicknameMap.put(senderNo, sender.getNickname());
        }

        // nextCursor 계산
        Long nextCursor = null;
        if (hasMore && !messages.isEmpty()) {
            nextCursor = messages.get(messages.size() - 1).getMessageNo();
        }

        // DTO 변환
        List<LoadMessagesResponse.MessageDto> messageDtos = messages.stream()
                .map(message -> LoadMessagesResponse.MessageDto.builder()
                        .messageNo(message.getMessageNo())
                        .senderNo(message.getSenderNo())
                        .senderName(senderNicknameMap.get(message.getSenderNo()))
                        .content(message.getContent())
                        .messageType(message.getMessageType())
                        .sentAt(message.getSentAt())
                        .readCount(0) // 나중에 구현
                        .build())
                .collect(Collectors.toList());

        return LoadMessagesResponse.builder()
                .messages(messageDtos)
                .nextCursor(nextCursor)
                .hasMore(hasMore)
                .build();
    }
}
