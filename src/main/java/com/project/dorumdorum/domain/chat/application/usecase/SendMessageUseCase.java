package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.request.SendMessageSocketRequest;
import com.project.dorumdorum.domain.chat.application.aop.NotificationPublish;
import com.project.dorumdorum.domain.chat.application.aop.NotificationSubject;
import com.project.dorumdorum.domain.chat.domain.entity.Message;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.chat.domain.service.MessageService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.transaction.support.TransactionSynchronization;
// import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMessageUseCase {

    private final MessageService messageService;
    private final MessageRoomService messageRoomService;
    private final ParticipantService participantService;
    private final UserService userService;

    @Transactional
    @NotificationPublish(
        subject = NotificationSubject.MESSAGE_SENT,
        event = "T(com.project.dorumdorum.domain.chat.application.event.MessageSentEvent).create(#result)"
    )
    public Message execute(String senderId, String roomId, SendMessageSocketRequest request) {

        /*boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("[FLOW][1_USECASE_START] thread={} transactionActive={}", Thread.currentThread().getName(), transactionActive);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    log.info("[FLOW][TRANSACTION_BEFORE_COMMIT] thread={} readOnly={}",
                        Thread.currentThread().getName(), readOnly);
                }

                @Override
                public void afterCommit() {
                    log.info("[FLOW][TRANSACTION_AFTER_COMMIT] thread={}", Thread.currentThread().getName());
                }

                @Override
                public void afterCompletion(int status) {
                    String result = switch (status) {
                        case STATUS_COMMITTED -> "COMMITTED";
                        case STATUS_ROLLED_BACK -> "ROLLED_BACK";
                        default -> "UNKNOWN";
                    };
                    log.info("[FLOW][TRANSACTION_AFTER_COMPLETION] thread={} status={}",
                        Thread.currentThread().getName(), result);
                }
            });
        }*/
        if (request == null || request.content() == null || request.content().isBlank()) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }

        // 유저 검증
        User sender = userService.findById(senderId); // 얘도 필요 없음 exist 가 필요
        MessageRoom messageRoom = messageRoomService.findById(roomId);
        if (messageRoom.getRoomStatus() != MessageRoomStatus.APPROVED) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }
        participantService.findByUserNoAndMessageRoomNo(sender, roomId); // 얘도 필요 없고 exist로

        // 이상한 이유: 목적이랑 메소드의 활용이 다르다 , findBy 는 객체를 찾아오기 위함
        // ** 메소드를 목적에 맞게 쓰자 **

        String content = request.content().trim();
        Message message = messageService.saveMessage(roomId, senderId, content);
        messageRoomService.updateLastMessage(messageRoom, content, message.getSentAt());
        // 그래서 이게 마지막
        // 이건 커넥션을 열고 잘 돼서 반환되어야 보내는 거러ㅏ서 보내는 게 더 중요하다면 먼저 보내자


        return message;
    } // 그러면 리스너를 없애고 애스팩트에서 비동기로 알림 보내기 호출??
    // 86에서 89ㄱ가 없어지고 SpEL(파라미터 추출) + AOP(이벤트로 받아오는 방법 알아보기!
    //
}
