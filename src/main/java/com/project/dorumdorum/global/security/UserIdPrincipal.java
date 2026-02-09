package com.project.dorumdorum.global.security;

import java.security.Principal;

public class UserIdPrincipal implements Principal {

    private final String userId;

    public UserIdPrincipal(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
