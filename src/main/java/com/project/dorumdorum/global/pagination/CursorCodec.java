package com.project.dorumdorum.global.pagination;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

public final class CursorCodec {

    public static String encode(LocalDateTime createdAt, String id) {
        String raw = createdAt + "|" + id;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(int remaining, LocalDateTime createdAt, String id) {
        String raw = remaining + "|" + createdAt + "|" + id;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static DecodedCursor decode(String cursor) {
        if (cursor == null || cursor.isBlank())
            return null;

        String raw = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
        String[] parts = raw.split("\\|");

        if (parts.length < 2)
            return null;

        LocalDateTime createdAt = LocalDateTime.parse(parts[parts.length - 2]);
        String id = parts[parts.length - 1];
        Integer remaining = parts.length >= 3 ? Integer.parseInt(parts[0]) : null;

        return new DecodedCursor(createdAt, id, remaining);
    }
}

