package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatReadReceiptResponse;
import com.project.dorumdorum.domain.chat.application.usecase.MarkChatRoomReadUseCase;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MarkChatRoomReadUseCase Unit Tests")
class MarkChatRoomReadUseCaseTest {

    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @InjectMocks private MarkChatRoomReadUseCase useCase;

    @Test
    @DisplayName("lastReadAt이 있으면 해당 시각 이후 메시지의 unread를 감소시키고 lastReadAt을 갱신한다")
    void execute_WhenLastReadAtExists_DecreasesFromLastReadAt() {
        ChatRoomMember member = mock(ChatRoomMember.class);
        LocalDateTime lastReadAt = LocalDateTime.now().minusHours(1);

        when(chatRoomMemberService.findByChatRoomNoAndUserNoForUpdate("cr-1", "u1")).thenReturn(member);
        when(member.getLastReadAt()).thenReturn(lastReadAt);

        useCase.execute("cr-1", "u1");

        verify(chatMessageService).decreaseUnreadCount("cr-1", lastReadAt, "u1");
        verify(chatRoomMemberService).updateLastReadAt(eq(member), any(LocalDateTime.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/chat-room/cr-1/read"), any(ChatReadReceiptResponse.class));
    }

    @Test
    @DisplayName("lastReadAt이 null이면 joinedAt 이후 메시지의 unread를 감소시킨다")
    void execute_WhenLastReadAtIsNull_DecreasesFromJoinedAt() {
        ChatRoomMember member = mock(ChatRoomMember.class);
        LocalDateTime joinedAt = LocalDateTime.now().minusDays(1);

        when(chatRoomMemberService.findByChatRoomNoAndUserNoForUpdate("cr-1", "u1")).thenReturn(member);
        when(member.getLastReadAt()).thenReturn(null);
        when(member.getJoinedAt()).thenReturn(joinedAt);

        useCase.execute("cr-1", "u1");

        verify(chatMessageService).decreaseUnreadCount("cr-1", joinedAt, "u1");
        verify(chatRoomMemberService).updateLastReadAt(eq(member), any(LocalDateTime.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/chat-room/cr-1/read"), any(ChatReadReceiptResponse.class));
    }
}
