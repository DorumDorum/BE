package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationRequestPublisher 단위 테스트")
class NotificationRequestPublisherTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private NotificationRequestPublisher publisher;

    @Test
    @DisplayName("publish는 NotificationRequestEvent를 생성해 ApplicationEventPublisher에 전달한다")
    void publish_PublishesEvent() {
        // when
        publisher.publish("user-1", "title", "body", NotificationType.ROOM_APPLICATION_APPROVED, "r1");

        // then
        var captor = forClass(NotificationRequestEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());

        NotificationRequestEvent event = captor.getValue();
        org.assertj.core.api.Assertions.assertThat(event.recipientNo()).isEqualTo("user-1");
        org.assertj.core.api.Assertions.assertThat(event.title()).isEqualTo("title");
        org.assertj.core.api.Assertions.assertThat(event.body()).isEqualTo("body");
        org.assertj.core.api.Assertions.assertThat(event.type()).isEqualTo(NotificationType.ROOM_APPLICATION_APPROVED);
        org.assertj.core.api.Assertions.assertThat(event.relatedId()).isEqualTo("r1");
    }
}
