package com.project.dorumdorum.domain.room.domain.entity;

public enum ConfirmStatus {
    PENDING("대기 중"),
    READY("준비 중");

    private final String description;

    ConfirmStatus(String description) {
        this.description = description;
    }

    public static String toDesc(ConfirmStatus confirmStatus) {
        return confirmStatus.description;
    }
}
