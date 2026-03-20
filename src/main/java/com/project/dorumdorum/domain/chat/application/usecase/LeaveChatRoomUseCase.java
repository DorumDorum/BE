package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.ChatErrorStatus.HOST_CANNOT_LEAVE;

@Service
@RequiredArgsConstructor
public class LeaveChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final RoommateService roommateService;

    @Transactional
    public void execute(String chatRoomNo, String userNo) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomNo(chatRoomNo);
        ChatRoomMember member = chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, userNo);
        long memberCount = chatRoomMemberService.countByChatRoom(chatRoom);

        if (memberCount > 1) {
            boolean isHost = roommateService.isHostOfRoom(userNo, chatRoom.getRoomNo());
            if (isHost) {
                throw new RestApiException(HOST_CANNOT_LEAVE);
            }
        }

        chatRoomMemberService.leave(member);
        roommateService.leaveRoom(userNo, chatRoom.getRoomNo());

        if (memberCount == 1) {
            // 마지막 멤버가 나가는 경우: 메시지 먼저 삭제 후 채팅방 삭제 (FK 제약 위반 방지)
            chatMessageService.deleteAllByChatRoom(chatRoom.getChatRoomNo());
            chatRoomService.delete(chatRoom);
        } else {
            chatMessageService.save(chatRoom, "SYSTEM", "룸메이트가 퇴장했습니다.", MessageType.SYSTEM, 0);
        }
    }
}
