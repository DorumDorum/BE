package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageResponse;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.ChatErrorStatus.HOST_CANNOT_LEAVE;

@Service
@RequiredArgsConstructor
public class LeaveChatRoomUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final RoomService roomService;
    private final RoommateService roommateService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

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

        // GROUP 채팅방인 경우에만 룸메이트 삭제 및 방 인원수 감소
        if (ChatRoomType.GROUP.equals(chatRoom.getChatRoomType())) {
            roommateService.leaveRoom(userNo, chatRoom.getRoomNo());
            Room room = roomService.findById(chatRoom.getRoomNo());
            room.minusCurrentMate();
        }

        if (memberCount == 1) {
            // 마지막 멤버가 나가는 경우: 메시지 먼저 삭제 후 채팅방 삭제 (FK 제약 위반 방지)
            chatMessageService.deleteAllByChatRoom(chatRoom.getChatRoomNo());
            chatRoomService.delete(chatRoom);
        } else {
            User leavingUser = userService.findById(userNo);
            String displayName = (leavingUser.getNickname() != null && !leavingUser.getNickname().isBlank())
                    ? leavingUser.getNickname() : leavingUser.getName();
            String content = displayName + "가 퇴장했습니다.";
            ChatMessage message = chatMessageService.save(chatRoom, "SYSTEM", content, MessageType.SYSTEM, 0);
            ChatMessageResponse response = new ChatMessageResponse(
                    message.getMessageNo(), chatRoomNo,
                    "SYSTEM", null, content, MessageType.SYSTEM.name(), message.getCreatedAt());
            messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoomNo, response);
        }
    }
}
