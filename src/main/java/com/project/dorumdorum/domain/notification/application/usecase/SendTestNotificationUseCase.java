package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.application.event.NotificationRequestPublisher;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class SendTestNotificationUseCase {

    private final NotificationRequestPublisher notificationRequestPublisher;

    /**
     * 테스트 푸시 알림 발행
     * - 랜덤 식별값으로 테스트 메시지를 생성
     * - 지정한 사용자에게 알림 발행 이벤트를 전송
     */
    @Transactional
    public void execute(String receiverNo) {
        int random = ThreadLocalRandom.current().nextInt(1000, 99999);
        notificationRequestPublisher.publish(
                receiverNo,
                "테스트 알림 #" + random,
                "테스트 전송 알림입니다. (" + random + ")",
                NotificationType.ROOM_APPLICATION_RECEIVED
        );
    }
}
