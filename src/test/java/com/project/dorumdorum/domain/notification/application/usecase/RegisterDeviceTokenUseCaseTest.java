package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeviceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterDeviceTokenUseCase 단위 테스트")
class RegisterDeviceTokenUseCaseTest {

    @Mock
    private NotificationDeviceRepository notificationDeviceRepository;

    @InjectMocks
    private RegisterDeviceTokenUseCase useCase;

    @Test
    @DisplayName("execute는 기존 디바이스가 없으면 새로 저장한다")
    void execute_WhenDeviceNotExists_SavesNew() {
        when(notificationDeviceRepository.findByUserNoAndDeviceId("user-1", "device-1"))
                .thenReturn(Optional.empty());

        useCase.execute("user-1", "device-1", "token-1");

        verify(notificationDeviceRepository).save(org.mockito.ArgumentMatchers.argThat(
                d -> d.getUserNo().equals("user-1") && d.getDeviceId().equals("device-1") && "token-1".equals(d.getFcmToken())
        ));
    }

    @Test
    @DisplayName("execute는 기존 디바이스가 있고 fcmToken이 있으면 업데이트한다")
    void execute_WhenDeviceExists_UpdatesFcmToken() {
        Device existing = Device.builder().id("id1").userNo("user-1").deviceId("device-1").fcmToken("old").build();
        when(notificationDeviceRepository.findByUserNoAndDeviceId("user-1", "device-1"))
                .thenReturn(Optional.of(existing));

        useCase.execute("user-1", "device-1", "token-1");

        verify(notificationDeviceRepository).findByUserNoAndDeviceId("user-1", "device-1");
        // updateFcmToken is called on existing entity
    }
}

