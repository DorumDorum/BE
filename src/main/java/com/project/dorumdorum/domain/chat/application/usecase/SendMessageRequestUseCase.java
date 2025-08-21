package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageRequestUseCase {



    public void execute(Long userNo, Long receiverNo, SendMessageRequest request) {


    }
}
