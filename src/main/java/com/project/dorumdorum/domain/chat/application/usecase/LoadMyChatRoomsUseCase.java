package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMyChatRoomsUseCase {

    private final ChatRoomService chatRoomService;

    @Transactional(readOnly = true)
    public List<ChatRoomSummary> execute(String userNo) {
        return chatRoomService.findMyChatRooms(userNo);
    }
}
