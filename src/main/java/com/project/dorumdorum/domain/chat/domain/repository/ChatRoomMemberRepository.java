package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, String> {

    Optional<ChatRoomMember> findByChatRoomAndUserNo(ChatRoom chatRoom, String userNo);

    boolean existsByChatRoomAndUserNo(ChatRoom chatRoom, String userNo);

    List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom);

    long countByChatRoom(ChatRoom chatRoom);

    boolean existsByChatRoom_ChatRoomNoAndUserNo(String chatRoomNo, String userNo);
}
