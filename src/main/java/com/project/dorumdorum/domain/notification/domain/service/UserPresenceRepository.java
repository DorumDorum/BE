package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;

public interface UserPresenceRepository {

    void setOnline(String userNo);

    void setOffline(String userNo);

    void setInChatroom(String userNo, String messageRoomNo);

    UserPresence getPresence(String userNo);
}
