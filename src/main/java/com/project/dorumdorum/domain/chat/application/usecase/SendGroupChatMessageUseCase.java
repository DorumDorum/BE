package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageResponse;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.notification.application.event.NotificationRequestPublisher;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.ratelimit.RateLimited;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendGroupChatMessageUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRequestPublisher notificationRequestPublisher;
    private final UserService userService;

    /**
     * 그룹 채팅 메시지 전송
     * - 발신자가 채팅방 멤버인지 검증
     * - 메시지를 저장하고 채팅방 최근 메시지를 갱신
     * - 웹소켓으로 메시지를 전송하고 다른 멤버에게 알림을 발행
     */
    @RateLimited(tag = "chat-message", key = "#senderNo + ':' + #chatRoomNo")
    @Transactional
    public void send(String chatRoomNo, String senderNo, String content) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomNo(chatRoomNo);

        // countByChatRoom + findByChatRoom을 분리 호출하면 같은 데이터를 위해 2번의 DB 쿼리가 발생함.
        // findByChatRoom으로 멤버 목록을 한 번만 조회하면 size()로 카운트를 계산할 수 있어 쿼리 1회 절감.
        List<ChatRoomMember> members = chatRoomMemberService.findByChatRoom(chatRoom);
        boolean isMember = members.stream().anyMatch(m -> m.getUserNo().equals(senderNo));
        if (!isMember) {
            // NOT_CHAT_ROOM_MEMBER RestApiException을 일관된 패턴으로 발생시킴
            chatRoomMemberService.validateMembership(chatRoom, senderNo);
        }

        int unreadCount = members.size() - 1;
        ChatMessage message = chatMessageService.save(
                chatRoom, senderNo, content, MessageType.TEXT, unreadCount);

        chatRoomService.updateLastMessage(chatRoom, content, senderNo, message.getCreatedAt());

        User user = userService.findById(senderNo);
        String nickname = user.getNickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = user.getName();
        }
        final String senderNickname = (nickname != null && !nickname.isBlank()) ? nickname : "알 수 없음";

        ChatMessageResponse response = new ChatMessageResponse(
                message.getMessageNo(),
                chatRoom.getChatRoomNo(),
                senderNo,
                senderNickname,
                content,
                MessageType.TEXT.name(),
                message.getCreatedAt(),
                message.getUnreadCount()
        );
        messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoomNo, response);

        members.stream()
                .filter(member -> !member.getUserNo().equals(senderNo))
                .forEach(member -> notificationRequestPublisher.publish(
                        member.getUserNo(),
                        senderNickname,
                        content,
                        NotificationType.NEW_MESSAGE_RECEIVED,
                        chatRoomNo
                ));
    }
}
