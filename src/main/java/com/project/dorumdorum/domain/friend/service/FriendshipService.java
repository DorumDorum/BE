package com.project.dorumdorum.domain.friend.service;

import com.project.dorumdorum.domain.friend.application.dto.response.LoadFriendshipResponse;
import com.project.dorumdorum.domain.friend.domain.entity.Friendship;
import com.project.dorumdorum.domain.friend.domain.repository.FriendshipRepository;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.FRIENDSHIP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final UserService userService;
    private final FriendshipRepository friendshipRepository;

    public Friendship findById(Long friendShipNo) {
        return friendshipRepository.findById(friendShipNo)
                .orElseThrow(() -> new RestApiException(FRIENDSHIP_NOT_FOUND));
    }

    public void addFriendship(Long fromUser, Long toUser) {
        Friendship newFriendship = Friendship.builder()
                .userNo(fromUser)
                .friendUserNo(toUser)
                .build();
        friendshipRepository.save(newFriendship);
    }

    public void deleteFriendship(Friendship friendship) {
        friendshipRepository.deleteById(friendship.getFriendshipNo());
    }

    public boolean areAlreadyFriends(Long fromUser, Long toUser) {
        return (!friendshipRepository.findByUserNoAndFriendUserNo(fromUser, toUser).isEmpty());
    }

    public List<LoadFriendshipResponse> loadFriendList(Long userNo) {
        List<Friendship> friendships = friendshipRepository.getFriendListByUserNo(userNo);

        return friendships.stream()
                .map(friendship -> {
                    Long friendUserNo = friendship.getFriendUserNo();
                    if(friendUserNo.equals(userNo))
                        friendUserNo = friendship.getUserNo();
                    User friend = userService.findById(friendUserNo);
                    return LoadFriendshipResponse.create(friendship, friend);
                })
                .toList();
    }
}
