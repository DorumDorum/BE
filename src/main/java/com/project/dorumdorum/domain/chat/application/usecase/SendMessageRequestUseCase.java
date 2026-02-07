package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.SendMessageRequest;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import com.project.dorumdorum.domain.chat.domain.service.MessageRoomService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageRequestUseCase {

    private final MessageRoomService messageRoomService;
    private final UserService userService;

    public void execute(Long userNo, Long receiverNo, SendMessageRequest request) {
//        userService.validateExistsById(userNo);

        // 채팅방 생성
        MessageRoom messageRoom = messageRoomService.create(request, MessageRoomType.DIRECT, MessageRoomStatus.REQUESTED);

        // 참가자 생성 + 매핑


        // 메세지 생성

        // SSE 알림 전송
    }
}
