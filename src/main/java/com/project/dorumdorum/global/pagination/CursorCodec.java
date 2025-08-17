package com.project.dorumdorum.global.pagination;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

public final class CursorCodec {

    public static String encode(LocalDateTime createdAt, Long id) {
        String raw = "null|" + createdAt + "|" + id;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeWithRemaining(int remaining, LocalDateTime createdAt, Long id) {
        String raw = remaining + "|" + createdAt + "|" + id;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static DecodedCursor decode(String cursor) {
        if (cursor == null || cursor.isBlank()) return null;
        String raw = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
        String[] parts = raw.split("\\|");
        Integer remaining = parts[0].equals("null") ? null : Integer.parseInt(parts[0]);
        LocalDateTime createdAt = LocalDateTime.parse(parts[1]);
        Long id = Long.parseLong(parts[2]);
        return new DecodedCursor(remaining, createdAt, id);
    }
}

