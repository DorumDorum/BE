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

import java.time.LocalDateTime;

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

    /**
     * 채팅방 퇴장 처리
     * - 채팅방 멤버를 조회하고 방장 퇴장 가능 여부를 검증
     * - 그룹 채팅방이면 룸메이트와 방 인원수를 함께 갱신
     * - 마지막 멤버면 방을 삭제하고, 아니면 퇴장 메시지를 전송
     */
    @Transactional
    public void execute(String chatRoomNo, String userNo) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomNo(chatRoomNo);
        ChatRoomMember member = chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, userNo);
        long memberCount = chatRoomMemberService.countByChatRoom(chatRoom);

        if (ChatRoomType.GROUP.equals(chatRoom.getChatRoomType())) {
            leaveGroupChatRoom(chatRoom, member, userNo, memberCount);
            return;
        }

        leaveDirectChatRoom(chatRoom, member, userNo, memberCount);
    }

    private void leaveGroupChatRoom(ChatRoom chatRoom, ChatRoomMember member, String userNo, long memberCount) {
        Room room = roomService.findByIdForUpdate(chatRoom.getRoomNo());

        if (memberCount > 1 && roommateService.isHost(userNo, room)) {
            throw new RestApiException(HOST_CANNOT_LEAVE);
        }

        if (memberCount > 1) {
            LocalDateTime fromTime = member.getLastReadAt() != null
                    ? member.getLastReadAt()
                    : member.getJoinedAt();
            chatMessageService.decreaseUnreadCount(chatRoom.getChatRoomNo(), fromTime, userNo);
        }

        chatRoomMemberService.leave(member);
        roommateService.leaveRoom(userNo, chatRoom.getRoomNo());
        room.minusCurrentMate();

        if (memberCount == 1) {
            chatMessageService.deleteAllByChatRoom(chatRoom.getChatRoomNo());
            chatRoomService.delete(chatRoom);
            return;
        }

        sendLeaveSystemMessage(chatRoom, userNo);
    }

    private void leaveDirectChatRoom(ChatRoom chatRoom, ChatRoomMember member, String userNo, long memberCount) {
        if (memberCount > 1 && roommateService.isHostOfRoom(userNo, chatRoom.getRoomNo())) {
            throw new RestApiException(HOST_CANNOT_LEAVE);
        }

        chatRoomMemberService.leave(member);

        if (memberCount == 1) {
            chatMessageService.deleteAllByChatRoom(chatRoom.getChatRoomNo());
            chatRoomService.delete(chatRoom);
            return;
        }

        sendLeaveSystemMessage(chatRoom, userNo);
    }

    private void sendLeaveSystemMessage(ChatRoom chatRoom, String userNo) {
        String chatRoomNo = chatRoom.getChatRoomNo();
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
