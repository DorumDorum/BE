package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.SendChatMessageRequest;
import com.project.dorumdorum.domain.chat.application.usecase.SendGroupChatMessageUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.ChatMessageApiSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController implements ChatMessageApiSpec {

    private final SendGroupChatMessageUseCase sendGroupChatMessageUseCase;

    @Override
    public void send(@DestinationVariable String chatRoomNo,
                     @Payload SendChatMessageRequest request,
                     SimpMessageHeaderAccessor headerAccessor) {
        String userNo = (String) headerAccessor.getSessionAttributes().get("userNo");
        sendGroupChatMessageUseCase.send(chatRoomNo, userNo, request.content());
    }
}
