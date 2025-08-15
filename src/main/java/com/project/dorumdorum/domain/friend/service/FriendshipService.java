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

    public void deleteFriendship(Long fromUser, Long toUser) {
        friendshipRepository.deleteByUserNoAndFriendUserNo(fromUser, toUser);
    }

    public boolean areAlreadyFriends(Long fromUser, Long toUser) {
        return (!friendshipRepository.findByUserNoAndFriendUserNo(fromUser, toUser).isEmpty());
    }
}
