package com.project.dorumdorum.domain.chat.application.dto.request;

public record LoadMessagesRequest(
    Long cursor,
    Integer size
) {
    public LoadMessagesRequest {
        if (size == null || size <= 0) {
            size = 20;
        }
    }

    public int getPageSize() {
        return size;
    }
}
