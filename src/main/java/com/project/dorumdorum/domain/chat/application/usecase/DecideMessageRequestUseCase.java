package com.project.dorumdorum.domain.chat.application.usecase;


import com.project.dorumdorum.domain.chat.application.dto.request.DecideMessageRequest;
import com.project.dorumdorum.domain.chat.application.dto.request.MessageRequestDecision;
import com.project.dorumdorum.domain.chat.application.event.ChatEventPublisher;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestDecidedEvent;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRequest;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRequestStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.service.MessageRequestService;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DecideMessageRequestUseCase {

    private final UserService userService;
    private final MessageRequestService messageRequestService;
    private final MessageRoomService messageRoomService;
    private final ParticipantService participantService;
    private final ChatEventPublisher chatEventPublisher;

    @Transactional
    public void execute(Long userId, Long messageRequestNo, DecideMessageRequest request) {

        if(request == null || request.messageRequestDecision() == null) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }

        // 유저 검증
        User receiver = userService.findById(userId);

        MessageRequest messageRequest = messageRequestService.findById(messageRequestNo);
        // 채팅 요청 수신자 검증
        if(!messageRequestService.isMessageRequestReceiver(messageRequest, userId)) {
            throw new RestApiException(GlobalErrorStatus.NOT_MESSAGE_REQUEST_RECEIVER);
        }
        // 채팅 요청 검증
        if(messageRequest.getStatus() != MessageRequestStatus.REQUESTED) {
            throw new RestApiException(GlobalErrorStatus.MESSAGEREQUEST_ALREADY_DECIDED);
        }


        MessageRoom messageRoom = messageRoomService.findById(messageRequest.getMessageRoomNo());

        // 수락일 때
        MessageRequestDecision decision = request.messageRequestDecision();

        if (decision == MessageRequestDecision.APPROVE) {
            // 채팅 요청 수락으로 변경
            messageRequestService.approveMessageRequest(messageRequest);
            // 채팅방 상태 수락으로 변경
            messageRoomService.updateStatusToApprove(messageRoom);
        } else {
            // 채팅 요청 거절로 변경
            messageRequestService.rejectMessageRequest(messageRequest);
            // 채팅방 상태 거절로 변경
            messageRoomService.updateStatusToReject(messageRoom);
            // 참여자 소프트 삭제 (수신자)
            participantService.softDelete(participantService.findByUserNoAndMessageRoomNo(receiver, messageRoom.getMessageRoomNo()));
            // 참여자 소프트 삭제 (요청자)
            participantService.softDelete(
                participantService.findByUserNoAndMessageRoomNo(
                    userService.findById(messageRequest.getSenderNo()), messageRoom.getMessageRoomNo()
                )
            );
        }

        MessageRequestDecidedEvent event = MessageRequestDecidedEvent.builder()
            .roomId(messageRoom.getMessageRoomNo())
            .senderId(messageRequest.getSenderNo())
            .receiverId(messageRequest.getReceiverNo())
            .decision(decision)
            .build();
        chatEventPublisher.publishMessageRequestDecided(event);
    }
}
