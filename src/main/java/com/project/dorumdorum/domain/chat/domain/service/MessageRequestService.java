package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRequest;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRequestStatus;
import com.project.dorumdorum.domain.chat.domain.repository.MessageRequestRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageRequestService {
    private final MessageRequestRepository messageRequestRepository;

    public void save(String senderNo, String receiverNo, String messageRoomNo) {
        MessageRequest entity = MessageRequest.builder()
            .senderNo(senderNo)
            .receiverNo(receiverNo)
            .messageRoomNo(messageRoomNo)
            .status(MessageRequestStatus.REQUESTED)
            .build();

        messageRequestRepository.save(entity);
    }

    public MessageRequest findById(String messageRequestId) {
        return messageRequestRepository.findById(messageRequestId)
            .orElseThrow(() -> new RestApiException(GlobalErrorStatus.MESSAGEREQUEST_NOT_FOUND));
    }

    public void approveMessageRequest(MessageRequest messageRequest) {
        messageRequest.approve();
    }

    public void rejectMessageRequest(MessageRequest messageRequest) {
        messageRequest.reject();
    }

    public boolean isMessageRequestReceiver(MessageRequest messageRequest, String receiverNo) {
        return messageRequest.getReceiverNo().equals(receiverNo);
    }

    public boolean isExistMessageRequest(String messageRequestNo) {
        return messageRequestRepository.existsById(messageRequestNo);
    }

}
