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

    /**
     * 내 채팅방 목록 조회
     * - 사용자가 속한 채팅방 목록을 조회
     * - 최근 메시지 기준 요약 정보로 반환
     */
    @Transactional(readOnly = true)
    public List<ChatRoomSummary> execute(String userNo) {
        return chatRoomService.findMyChatRooms(userNo);
    }
}
