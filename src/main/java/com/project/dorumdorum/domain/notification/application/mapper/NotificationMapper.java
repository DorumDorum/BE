package com.project.dorumdorum.domain.notification.application.mapper;

import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "isRead", source = "read")
    @Mapping(target = "redirectPath", expression = "java(com.project.dorumdorum.domain.notification.application.helper.NotificationRedirectHelper.resolvePath(notification.getType(), notification.getRelatedId()))")
    LoadNotificationResponse toResponse(Notification notification);

    List<LoadNotificationResponse> toResponseList(List<Notification> notifications);
}
