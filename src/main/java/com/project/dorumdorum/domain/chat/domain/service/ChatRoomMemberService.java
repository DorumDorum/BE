package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomMemberRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.dorumdorum.global.exception.code.status.ChatErrorStatus.*;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatRoomMember join(ChatRoom chatRoom, String userNo) {
        if (chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, userNo)) {
            throw new RestApiException(ALREADY_CHAT_ROOM_MEMBER);
        }
        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .userNo(userNo)
                .build();
        return chatRoomMemberRepository.save(member);
    }

    public ChatRoomMember findByChatRoomAndUserNo(ChatRoom chatRoom, String userNo) {
        return chatRoomMemberRepository.findByChatRoomAndUserNo(chatRoom, userNo)
                .orElseThrow(() -> new RestApiException(CHAT_ROOM_MEMBER_NOT_FOUND));
    }

    public Optional<ChatRoomMember> findOptionalByChatRoomAndUserNo(ChatRoom chatRoom, String userNo) {
        return chatRoomMemberRepository.findByChatRoomAndUserNo(chatRoom, userNo);
    }

    public ChatRoomMember findByChatRoomNoAndUserNoForUpdate(String chatRoomNo, String userNo) {
        return chatRoomMemberRepository.findByChatRoomNoAndUserNoForUpdate(chatRoomNo, userNo)
                .orElseThrow(() -> new RestApiException(CHAT_ROOM_MEMBER_NOT_FOUND));
    }

    public boolean isMember(ChatRoom chatRoom, String userNo) {
        return chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, userNo);
    }

    public void validateMembership(ChatRoom chatRoom, String userNo) {
        if (!chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, userNo)) {
            throw new RestApiException(NOT_CHAT_ROOM_MEMBER);
        }
    }

    public List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom) {
        return chatRoomMemberRepository.findByChatRoom(chatRoom);
    }

    public long countByChatRoom(ChatRoom chatRoom) {
        return chatRoomMemberRepository.countByChatRoom(chatRoom);
    }

    public boolean isMemberByChatRoomNo(String chatRoomNo, String userNo) {
        return chatRoomMemberRepository.existsByChatRoom_ChatRoomNoAndUserNo(chatRoomNo, userNo);
    }

    public void leave(ChatRoomMember member) {
        chatRoomMemberRepository.delete(member);
    }

    public void deleteAllByChatRoom(ChatRoom chatRoom) {
        chatRoomMemberRepository.deleteAllByChatRoom(chatRoom);
    }

    public void updateLastReadAt(ChatRoomMember member, LocalDateTime readAt) {
        member.updateLastReadAt(readAt);
        chatRoomMemberRepository.save(member);
    }
}
