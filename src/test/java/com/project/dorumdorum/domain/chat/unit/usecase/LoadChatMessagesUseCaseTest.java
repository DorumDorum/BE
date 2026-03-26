package com.project.dorumdorum.domain.chat.unit.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageSummary;
import com.project.dorumdorum.domain.chat.application.usecase.LoadChatMessagesUseCase;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.global.pagination.CursorPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadChatMessagesUseCase Unit Tests")
class LoadChatMessagesUseCaseTest {

    @Mock private ChatRoomService chatRoomService;
    @Mock private ChatRoomMemberService chatRoomMemberService;
    @Mock private ChatMessageService chatMessageService;
    @InjectMocks private LoadChatMessagesUseCase useCase;

    @Test
    @DisplayName("메시지가 없으면 빈 CursorPage를 반환한다")
    void execute_WhenNoMessages_ReturnsEmptyCursorPage() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(member.getJoinedAt()).thenReturn(LocalDateTime.now().minusDays(1));
        when(chatMessageService.findMessages(eq("cr-1"), any(), isNull(), isNull(), anyInt()))
                .thenReturn(List.of());

        CursorPage<ChatMessageSummary> result = useCase.execute("cr-1", "u1", null);

        assertThat(result.items()).isEmpty();
        assertThat(result.hasNext()).isFalse();
        assertThat(result.nextCursor()).isNull();
    }

    @Test
    @DisplayName("메시지가 있으면 CursorPage로 반환한다")
    void execute_WhenMessagesExist_ReturnsCursorPage() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        ChatMessage message = mock(ChatMessage.class);
        LocalDateTime now = LocalDateTime.now();

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(member.getJoinedAt()).thenReturn(now.minusDays(1));
        when(message.getMessageNo()).thenReturn("msg-1");
        when(message.getSenderNo()).thenReturn("u1");
        when(message.getContent()).thenReturn("hello");
        when(message.getMessageType()).thenReturn(MessageType.TEXT);
        when(message.getCreatedAt()).thenReturn(now);
        when(message.getUnreadCount()).thenReturn(2);
        when(chatMessageService.findMessages(eq("cr-1"), any(), isNull(), isNull(), anyInt()))
                .thenReturn(List.of(message));

        CursorPage<ChatMessageSummary> result = useCase.execute("cr-1", "u1", null);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).messageNo()).isEqualTo("msg-1");
        assertThat(result.items().get(0).content()).isEqualTo("hello");
        assertThat(result.items().get(0).unreadCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("cursor가 있으면 cursorCreatedAt, cursorId를 디코딩하여 findMessages에 전달한다")
    void execute_WithCursor_PassesDecodedCursorParams() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(member.getJoinedAt()).thenReturn(LocalDateTime.now().minusDays(1));
        // cursor가 있으면 cursorCreatedAt, cursorId 모두 non-null로 전달
        when(chatMessageService.findMessages(eq("cr-1"), any(), notNull(), notNull(), anyInt()))
                .thenReturn(List.of());

        // CursorCodec.encode("2026-03-19T10:00", "msg-1")
        String cursor = com.project.dorumdorum.global.pagination.CursorCodec
                .encode(LocalDateTime.of(2026, 3, 19, 10, 0), "msg-1");

        CursorPage<ChatMessageSummary> result = useCase.execute("cr-1", "u1", cursor);

        assertThat(result.items()).isEmpty();
        verify(chatMessageService).findMessages(eq("cr-1"), any(), notNull(), notNull(), anyInt());
    }

    @Test
    @DisplayName("메시지가 LIMIT+1개 반환되면 hasNext=true이고 nextCursor가 non-null이다")
    void execute_WhenResultsExceedLimit_ReturnsHasNextTrueAndNextCursorNonNull() {
        ChatRoom chatRoom = mock(ChatRoom.class);
        ChatRoomMember member = mock(ChatRoomMember.class);
        LocalDateTime now = LocalDateTime.now();

        when(chatRoomService.findByChatRoomNo("cr-1")).thenReturn(chatRoom);
        when(chatRoomMemberService.findByChatRoomAndUserNo(chatRoom, "u1")).thenReturn(member);
        when(member.getJoinedAt()).thenReturn(now.minusDays(1));

        List<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            ChatMessage msg = mock(ChatMessage.class);
            when(msg.getMessageNo()).thenReturn("msg-" + i);
            when(msg.getSenderNo()).thenReturn("u1");
            when(msg.getContent()).thenReturn("content-" + i);
            when(msg.getMessageType()).thenReturn(MessageType.TEXT);
            when(msg.getCreatedAt()).thenReturn(now.minusMinutes(i));
            when(msg.getUnreadCount()).thenReturn(0);
            messages.add(msg);
        }
        when(chatMessageService.findMessages(eq("cr-1"), any(), isNull(), isNull(), anyInt()))
                .thenReturn(messages);

        CursorPage<ChatMessageSummary> result = useCase.execute("cr-1", "u1", null);

        assertThat(result.items()).hasSize(30);
        assertThat(result.hasNext()).isTrue();
        assertThat(result.nextCursor()).isNotNull();
    }
}
