package com.project.dorumdorum.testsupport;

import org.testcontainers.DockerClientFactory;

/**
 * Utility helpers for integration tests that rely on Testcontainers.
 */
public final class TestcontainersSupport {

    private static final boolean DOCKER_AVAILABLE = checkDockerAvailability();
    private static final boolean CI = detectCiEnvironment();

    private TestcontainersSupport() {
    }

    public static boolean isDockerAvailable() {
        return DOCKER_AVAILABLE;
    }

    public static boolean isCiEnvironment() {
        return CI;
    }

    /**
     * Returns {@code true} when Docker is ready for use. If Docker is unavailable in CI, throws an
     * {@link IllegalStateException} so the build fails instead of silently skipping tests.
     */
    public static boolean requireDockerOrSkip(String testName) {
        if (DOCKER_AVAILABLE) {
            return true;
        }
        if (CI) {
            throw new IllegalStateException("Docker is required for " + testName + " in CI");
        }
        return false;
    }

    private static boolean checkDockerAvailability() {
        try {
            DockerClientFactory.instance().client();
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static boolean detectCiEnvironment() {
        String ciEnv = System.getenv("CI");
        return ciEnv != null && Boolean.parseBoolean(ciEnv);
    }
}
