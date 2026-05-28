package com.project.dorumdorum.domain.chat.unit.service;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomMemberRepository;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatRoomMemberService Unit Tests")
class ChatRoomMemberServiceTest {

    @Mock private ChatRoomMemberRepository chatRoomMemberRepository;
    @InjectMocks private ChatRoomMemberService service;

    private final ChatRoom chatRoom = ChatRoom.builder().roomNo("room-1").build();

    @Test
    @DisplayName("join: 중복이 아니면 멤버를 저장하고 반환")
    void join_WhenNotDuplicate_SavesAndReturns() {
        when(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, "user-1")).thenReturn(false);
        ChatRoomMember saved = ChatRoomMember.builder().chatRoom(chatRoom).userNo("user-1").build();
        when(chatRoomMemberRepository.save(any(ChatRoomMember.class))).thenReturn(saved);

        ChatRoomMember result = service.join(chatRoom, "user-1");

        assertThat(result.getUserNo()).isEqualTo("user-1");
        verify(chatRoomMemberRepository).save(any(ChatRoomMember.class));
    }

    @Test
    @DisplayName("join: 이미 멤버면 RestApiException")
    void join_WhenDuplicate_Throws() {
        when(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, "user-1")).thenReturn(true);

        assertThatThrownBy(() -> service.join(chatRoom, "user-1"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("findByChatRoomAndUserNo: 존재하면 반환")
    void findByChatRoomAndUserNo_WhenExists_Returns() {
        ChatRoomMember member = ChatRoomMember.builder().chatRoom(chatRoom).userNo("user-1").build();
        when(chatRoomMemberRepository.findByChatRoomAndUserNo(chatRoom, "user-1")).thenReturn(Optional.of(member));

        ChatRoomMember result = service.findByChatRoomAndUserNo(chatRoom, "user-1");

        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("findByChatRoomAndUserNo: 존재하지 않으면 RestApiException")
    void findByChatRoomAndUserNo_WhenMissing_Throws() {
        when(chatRoomMemberRepository.findByChatRoomAndUserNo(chatRoom, "user-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByChatRoomAndUserNo(chatRoom, "user-1"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("findByChatRoomNoAndUserNoForUpdate: 존재하면 반환")
    void findByChatRoomNoAndUserNoForUpdate_WhenExists_Returns() {
        ChatRoomMember member = ChatRoomMember.builder().chatRoom(chatRoom).userNo("user-1").build();
        when(chatRoomMemberRepository.findByChatRoomNoAndUserNoForUpdate("cr-1", "user-1"))
                .thenReturn(Optional.of(member));

        ChatRoomMember result = service.findByChatRoomNoAndUserNoForUpdate("cr-1", "user-1");

        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("findByChatRoomNoAndUserNoForUpdate: 존재하지 않으면 RestApiException")
    void findByChatRoomNoAndUserNoForUpdate_WhenMissing_Throws() {
        when(chatRoomMemberRepository.findByChatRoomNoAndUserNoForUpdate("cr-1", "user-1"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByChatRoomNoAndUserNoForUpdate("cr-1", "user-1"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("validateMembership: 멤버가 아니면 RestApiException")
    void validateMembership_WhenNotMember_Throws() {
        when(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, "user-1")).thenReturn(false);

        assertThatThrownBy(() -> service.validateMembership(chatRoom, "user-1"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("validateMembership: 멤버면 예외 없음")
    void validateMembership_WhenMember_NoException() {
        when(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, "user-1")).thenReturn(true);

        service.validateMembership(chatRoom, "user-1"); // no exception
    }

    @Test
    @DisplayName("countByChatRoom: repository에 위임")
    void countByChatRoom_DelegatesToRepository() {
        when(chatRoomMemberRepository.countByChatRoom(chatRoom)).thenReturn(3L);

        assertThat(service.countByChatRoom(chatRoom)).isEqualTo(3L);
    }

    @Test
    @DisplayName("leave: 멤버를 소프트 삭제하고 저장")
    void leave_SoftDeletesAndSaves() {
        ChatRoomMember member = ChatRoomMember.builder().chatRoom(chatRoom).userNo("user-1").build();

        service.leave(member);

        assertThat(member.getDeletedAt()).isNotNull();
        verify(chatRoomMemberRepository).save(member);
    }

    @Test
    @DisplayName("updateLastReadAt: member의 lastReadAt을 갱신")
    void updateLastReadAt_UpdatesField() {
        ChatRoomMember member = ChatRoomMember.builder().chatRoom(chatRoom).userNo("user-1").build();
        LocalDateTime readAt = LocalDateTime.of(2026, 3, 19, 12, 0);

        service.updateLastReadAt(member, readAt);

        assertThat(member.getLastReadAt()).isEqualTo(readAt);
    }

    @Test
    @DisplayName("isMemberByChatRoomNo: 채팅방 번호와 유저 번호로 멤버 여부를 반환한다")
    void isMemberByChatRoomNo_WhenMemberExists_ReturnsTrue() {
        when(chatRoomMemberRepository.existsByChatRoom_ChatRoomNoAndUserNo("cr-1", "user-1")).thenReturn(true);

        boolean result = service.isMemberByChatRoomNo("cr-1", "user-1");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isMemberByChatRoomNo: 멤버가 아닌 경우 false를 반환한다")
    void isMemberByChatRoomNo_WhenNotMember_ReturnsFalse() {
        when(chatRoomMemberRepository.existsByChatRoom_ChatRoomNoAndUserNo("cr-1", "user-1")).thenReturn(false);

        boolean result = service.isMemberByChatRoomNo("cr-1", "user-1");

        assertThat(result).isFalse();
    }
}
