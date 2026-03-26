package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.ChatErrorStatus.NOT_CHAT_ROOM_MEMBER;

@Service
@Transactional
@RequiredArgsConstructor
public class GetOrCreateDirectChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final RoomService roomService;

    /**
     * 지원자-방장 1:1 채팅방 조회 또는 생성
     * - 이미 존재하면 기존 채팅방 반환
     * - 없으면 생성 후 반환
     * - 호출자는 방장 또는 지원자여야 함
     */
    public String execute(String callerUserNo, String roomNo, String applicantUserNo) {
        Room room = roomService.findById(roomNo);
        String hostUserNo = room.getHostUserNo();

        if (!hostUserNo.equals(callerUserNo) && !applicantUserNo.equals(callerUserNo)) {
            throw new RestApiException(NOT_CHAT_ROOM_MEMBER);
        }

        return chatRoomService.findDirectChatRoom(roomNo, applicantUserNo)
                .map(ChatRoom::getChatRoomNo)
                .orElseGet(() -> {
                    ChatRoom chatRoom = chatRoomService.createDirectChatRoom(roomNo, applicantUserNo);
                    chatRoomMemberService.join(chatRoom, applicantUserNo);
                    chatRoomMemberService.join(chatRoom, hostUserNo);
                    return chatRoom.getChatRoomNo();
                });
    }

    /**
     * 이벤트 리스너에서 사용 — 인증 없이 채팅방 생성 (방 지원 시 자동 생성)
     */
    public String createIfAbsent(String roomNo, String applicantUserNo, String hostUserNo) {
        return chatRoomService.findDirectChatRoom(roomNo, applicantUserNo)
                .map(ChatRoom::getChatRoomNo)
                .orElseGet(() -> {
                    ChatRoom chatRoom = chatRoomService.createDirectChatRoom(roomNo, applicantUserNo);
                    chatRoomMemberService.join(chatRoom, applicantUserNo);
                    chatRoomMemberService.join(chatRoom, hostUserNo);
                    return chatRoom.getChatRoomNo();
                });
    }
}
