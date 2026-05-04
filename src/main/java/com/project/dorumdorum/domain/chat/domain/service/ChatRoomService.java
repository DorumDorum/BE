package com.project.dorumdorum.domain.chat.domain.service;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.dorumdorum.global.exception.code.status.ChatErrorStatus.CHAT_ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom create(String roomNo) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomNo(roomNo)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findByChatRoomNo(String chatRoomNo) {
        return chatRoomRepository.findById(chatRoomNo)
                .orElseThrow(() -> new RestApiException(CHAT_ROOM_NOT_FOUND));
    }

    public Optional<ChatRoom> findByRoomNo(String roomNo) {
        return chatRoomRepository.findByRoomNoAndChatRoomType(roomNo, ChatRoomType.GROUP);
    }

    public boolean existsByRoomNo(String roomNo) {
        return chatRoomRepository.existsByRoomNo(roomNo);
    }

    public ChatRoom createDirectChatRoom(String roomNo, String applicantUserNo) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomNo(roomNo)
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo(applicantUserNo)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    public boolean existsDirectChatRoom(String roomNo, String applicantUserNo) {
        return chatRoomRepository.existsByRoomNoAndChatRoomTypeAndApplicantUserNo(
                roomNo, ChatRoomType.DIRECT, applicantUserNo);
    }

    public Optional<ChatRoom> findDirectChatRoom(String roomNo, String applicantUserNo) {
        return chatRoomRepository.findByRoomNoAndChatRoomTypeAndApplicantUserNo(
                roomNo, ChatRoomType.DIRECT, applicantUserNo);
    }

    public void updateLastMessage(ChatRoom chatRoom, String content, String senderNo, LocalDateTime sentAt) {
        chatRoom.updateLastMessage(content, senderNo, sentAt);
    }

    public void delete(ChatRoom chatRoom) {
        chatRoom.delete();
        chatRoomRepository.save(chatRoom);
    }

    public void deleteByChatRoomNo(String chatRoomNo) {
        chatRoomRepository.deleteByChatRoomNo(chatRoomNo);
    }

    public List<ChatRoom> findAllByRoomNo(String roomNo) {
        return chatRoomRepository.findAllByRoomNo(roomNo);
    }

    public List<ChatRoomSummary> findMyChatRooms(String userNo) {
        return chatRoomRepository.findMyChatRooms(userNo);
    }
}
