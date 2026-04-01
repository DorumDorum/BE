package com.project.dorumdorum.testsupport;

import org.testcontainers.DockerClientFactory;

/**
 * Utility helpers for integration tests that rely on Testcontainers.
 */
public final class TestcontainersSupport {

    private static final boolean DOCKER_AVAILABLE = checkDockerAvailability();

    private TestcontainersSupport() {
    }

    public static boolean isDockerAvailable() {
        return DOCKER_AVAILABLE;
    }

    private static boolean checkDockerAvailability() {
        try {
            DockerClientFactory.instance().client();
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
