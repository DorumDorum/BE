package com.project.dorumdorum.global.exception;

public record StompErrorResponse(
        String code,
        String message
) {}
