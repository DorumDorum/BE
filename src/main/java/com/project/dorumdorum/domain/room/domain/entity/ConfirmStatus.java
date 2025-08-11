package com.project.dorumdorum.domain.room.domain.entity;

public enum ConfirmStatus {
    PENDING("배정 대기 중"),
    ACCEPTED("방 배정 수락"),
    COMPLETED("방 배정 완료");

    private final String description;

    ConfirmStatus(String description) {
        this.description = description;
    }

    public static String toDesc(ConfirmStatus confirmStatus) {
        return confirmStatus.description;
    }
}
