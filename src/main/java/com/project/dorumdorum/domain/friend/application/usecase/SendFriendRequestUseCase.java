package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.service.FriendRequestService;
import com.project.dorumdorum.domain.friend.service.FriendshipService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.domain.friend.application.dto.request.SendFriendRequest;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendFriendRequestUseCase {

    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final FriendshipService friendshipService;
    private final NotificationService notificationService;

    @Transactional
    public void execute(Long fromUser, SendFriendRequest request) {
        userService.validateExistsById(fromUser);
        Long toUserNo = request.toUser();

        if(fromUser.equals(toUserNo))
            throw new RestApiException(GlobalErrorStatus.FRIEND_SELF_REQUEST);

        if(friendRequestService.existFriendRequestByFromUser(fromUser))
            throw new RestApiException(GlobalErrorStatus.DUPLICATE_FRIEND_REQUEST);

        if(friendshipService.areAlreadyFriends(fromUser, toUserNo))
            throw new RestApiException(GlobalErrorStatus.ALREADY_FRIEND);

        FriendRequest friendRequest = friendRequestService.saveRequest(fromUser, toUserNo);

        // 친구 요청 알림 발송
        User sender = userService.findById(fromUser);
        notificationService.sendNotification(
                toUserNo,
                sender.getNickname() + "님이 친구 요청을 보냈어요",
                "친구 신청 목록 탭에서 수락/거절할 수 있어요",
                Map.of(
                        "type", "FRIEND_REQUEST",
                        "friendRequestNo", friendRequest.getFriendRequestNo().toString(),
                        "fromUserNo", fromUser.toString(),
                        "fromNickname", sender.getNickname() == null ? "" : sender.getNickname()
                ),
                null
        );
    }

}
