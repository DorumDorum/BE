package com.project.dorumdorum.domain.user.domain.entity;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER")
    ;

    private final String name;

    Role(String name) {
        this.name = name;
    }
}