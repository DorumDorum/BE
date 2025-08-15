package com.project.dorumdorum.domain.friend.service;

import com.project.dorumdorum.domain.friend.domain.entity.Friendship;
import com.project.dorumdorum.domain.friend.domain.repository.FriendshipRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    public void addFriendship(Long fromUser, Long toUser) {
        Friendship newFriendship = Friendship.builder()
                .userNo(fromUser)
                .friendUserNo(toUser)
                .build();
        friendshipRepository.save(newFriendship);
    }

    public void removeFriendship(Long fromUser, Long toUser) {
        
    }
}
