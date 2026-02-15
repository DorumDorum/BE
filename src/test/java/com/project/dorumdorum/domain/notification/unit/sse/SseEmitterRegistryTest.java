package com.project.dorumdorum.domain.notification.unit.sse;

import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SseEmitterRegistry Unit Tests")
class SseEmitterRegistryTest {

    @Test
    @DisplayName("Should register and get emitter")
    void registerAndGet_Works() {
        SseEmitterRegistry registry = new SseEmitterRegistry();
        SseEmitter emitter = new SseEmitter(1000L);

        registry.register("u1", emitter);

        assertThat(registry.get("u1")).contains(emitter);
    }

    @Test
    @DisplayName("Should remove emitter by user id")
    void remove_DeletesEmitter() {
        SseEmitterRegistry registry = new SseEmitterRegistry();
        registry.register("u1", new SseEmitter(1000L));

        registry.remove("u1");

        assertThat(registry.get("u1")).isEmpty();
    }

    @Test
    @DisplayName("Should replace previous emitter for same user")
    void register_WhenExists_ReplacesEmitter() {
        SseEmitterRegistry registry = new SseEmitterRegistry();
        SseEmitter first = new SseEmitter(1000L);
        SseEmitter second = new SseEmitter(1000L);
        registry.register("u1", first);

        registry.register("u1", second);

        assertThat(registry.get("u1")).contains(second);
    }
}
