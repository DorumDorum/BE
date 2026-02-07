package com.project.dorumdorum.global.security;

import java.security.Principal;

public class UserIdPrincipal implements Principal {

    private final Long userId;

    public UserIdPrincipal(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
