package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeviceRepository;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationRepository;
import com.project.dorumdorum.domain.room.domain.repository.RoomLikeRepository;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestRepository;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateRepository;
import com.project.dorumdorum.domain.user.application.dto.request.DeleteAccountRequest;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus.ACTIVE_ROOM_OWNER;

@Service
@RequiredArgsConstructor
public class DeleteAccountUseCase {

    private final UserService userService;
    private final RoomRepository roomRepository;
    private final RoomLikeRepository roomLikeRepository;
    private final RoomRequestRepository roomRequestRepository;
    private final RoommateRepository roommateRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationDeviceRepository notificationDeviceRepository;

    @Transactional
    public void execute(String userNo, DeleteAccountRequest request) {
        if (roomRepository.existsByHostUserNoAndDeletedAtIsNull(userNo)) {
            throw new RestApiException(ACTIVE_ROOM_OWNER);
        }

        User user = userService.findById(userNo);
        roomLikeRepository.deleteAllByUserNo(userNo);
        roomRequestRepository.deleteAllByUserNo(userNo);
        roommateRepository.deleteAllByUserNo(userNo);
        notificationRepository.deleteAllByRecipientNo(userNo);
        notificationDeviceRepository.deleteAllByUserNo(userNo);
        user.anonymizeForWithdrawal();
        user.delete();
    }
}
