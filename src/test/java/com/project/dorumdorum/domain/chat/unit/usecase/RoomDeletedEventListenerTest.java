package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.NotificationMessage;
import com.project.dorumdorum.domain.chat.application.event.RoomDeletedEventListener;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.room.application.event.RoomDeletedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomDeletedEventListener Unit Tests")
class RoomDeletedEventListenerTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @InjectMocks private RoomDeletedEventListener listener;

    @Test
    @DisplayName("GROUP 채팅방 — /topic/ 브로드캐스트 후 메시지·멤버·채팅방 순서로 삭제")
    void handle_GroupChatRoom_BroadcastsAndDeletesInOrder() {
        ChatRoom groupRoom = ChatRoom.builder()
                .chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP)
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(groupRoom));

        listener.handle(new RoomDeletedEvent("r1"));

        verify(messagingTemplate).convertAndSend(eq("/topic/chat-room/cr-1"), any(NotificationMessage.class));
        verify(messagingTemplate, never()).convertAndSendToUser(any(), any(), any());

        InOrder inOrder = inOrder(chatMessageService, chatRoomMemberService, chatRoomService);
        inOrder.verify(chatMessageService).deleteAllByChatRoom("cr-1");
        inOrder.verify(chatRoomMemberService).deleteAllByChatRoom(groupRoom);
        inOrder.verify(chatRoomService).delete(groupRoom);
    }

    @Test
    @DisplayName("DIRECT 채팅방 — /topic/ 브로드캐스트 + 지원자에게 /queue/notification 개인 알림")
    void handle_DirectChatRoom_BroadcastsAndNotifiesApplicant() {
        ChatRoom directRoom = ChatRoom.builder()
                .chatRoomNo("cr-2").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo("applicant-1")
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(directRoom));

        listener.handle(new RoomDeletedEvent("r1"));

        verify(messagingTemplate).convertAndSend(eq("/topic/chat-room/cr-2"), any(NotificationMessage.class));
        verify(messagingTemplate).convertAndSendToUser(eq("applicant-1"), eq("/queue/notification"), any(NotificationMessage.class));
    }

    @Test
    @DisplayName("DIRECT 채팅방에서 applicantUserNo가 null이면 개인 알림 전송 안 함")
    void handle_DirectChatRoomWithNullApplicant_NoPersonalNotification() {
        ChatRoom directRoom = ChatRoom.builder()
                .chatRoomNo("cr-3").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo(null)
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(directRoom));

        listener.handle(new RoomDeletedEvent("r1"));

        verify(messagingTemplate, never()).convertAndSendToUser(any(), any(), any());
    }

    @Test
    @DisplayName("채팅방이 없으면 삭제 및 브로드캐스트 없이 종료")
    void handle_NoChatRooms_DoesNothing() {
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of());

        listener.handle(new RoomDeletedEvent("r1"));

        verify(messagingTemplate, never()).convertAndSend(any(String.class), any(Object.class));
        verify(chatMessageService, never()).deleteAllByChatRoom(any());
        verify(chatRoomMemberService, never()).deleteAllByChatRoom(any());
    }

    @Test
    @DisplayName("WebSocket 브로드캐스트 실패 시 예외를 무시하고 삭제 처리 계속 진행")
    void handle_WebSocketBroadcastFails_ContinuesWithDeletion() {
        ChatRoom groupRoom = ChatRoom.builder()
                .chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP)
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(groupRoom));
        doThrow(new RuntimeException("WebSocket error"))
                .when(messagingTemplate).convertAndSend(anyString(), any(Object.class));

        listener.handle(new RoomDeletedEvent("r1"));

        verify(chatMessageService).deleteAllByChatRoom("cr-1");
        verify(chatRoomMemberService).deleteAllByChatRoom(groupRoom);
        verify(chatRoomService).delete(groupRoom);
    }

    @Test
    @DisplayName("개인 알림 전송 실패 시 예외를 무시하고 삭제 처리 계속 진행")
    void handle_PersonalNotificationFails_ContinuesWithDeletion() {
        ChatRoom directRoom = ChatRoom.builder()
                .chatRoomNo("cr-2").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo("applicant-1")
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(directRoom));
        doThrow(new RuntimeException("queue error"))
                .when(messagingTemplate).convertAndSendToUser(any(), any(), any());

        listener.handle(new RoomDeletedEvent("r1"));

        verify(chatMessageService).deleteAllByChatRoom("cr-2");
        verify(chatRoomMemberService).deleteAllByChatRoom(directRoom);
        verify(chatRoomService).delete(directRoom);
    }

    @Test
    @DisplayName("복수 채팅방(GROUP + DIRECT)이 있으면 모두 처리")
    void handle_MultipleRooms_ProcessesAll() {
        ChatRoom groupRoom = ChatRoom.builder()
                .chatRoomNo("cr-1").roomNo("r1")
                .chatRoomType(ChatRoomType.GROUP)
                .build();
        ChatRoom directRoom = ChatRoom.builder()
                .chatRoomNo("cr-2").roomNo("r1")
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo("applicant-1")
                .build();
        when(chatRoomService.findAllByRoomNo("r1")).thenReturn(List.of(groupRoom, directRoom));

        listener.handle(new RoomDeletedEvent("r1"));

        verify(chatRoomService).delete(groupRoom);
        verify(chatRoomService).delete(directRoom);
        verify(messagingTemplate).convertAndSendToUser(eq("applicant-1"), eq("/queue/notification"), any());
    }
}
