package com.project.dorumdorum.domain.notification.mapper;

import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "isRead", source = "read")
    @Mapping(target = "redirectPath", expression = "java(resolveRedirectPath(notification.getType(), notification.getRelatedId()))")
    LoadNotificationResponse toResponse(Notification notification);

    List<LoadNotificationResponse> toResponseList(List<Notification> notifications);

    @Mapping(target = "redirectPath", expression = "java(resolveRedirectPath(notification.getType(), notification.getRelatedId()))")
    NotificationDeliveryPayload toDeliveryPayload(Notification notification);

    default String resolveRedirectPath(NotificationType type, String relatedId) {
        if (type == null)
            return null;
        String template = type.getPathTemplate();
        if (!template.contains("{"))
            return template;
        if (relatedId == null || relatedId.isBlank())
            return null;
        return template.replaceAll("\\{\\w+\\}", relatedId);
    }
}
