package com.project.dorumdorum.domain.user.fixture;

public class TokenFixture {

    public static String createValidAccessToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIwMDAwMDAwMDAwMDAwMDAxIiwiZW1haWwiOiJ0ZXN0QHVuaXZlcnNpdHkuYWMua3IiLCJyb2xlIjoiVVNFUiIsImV4cCI6OTk5OTk5OTk5OX0.test-signature";
    }

    public static String createValidRefreshToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIwMDAwMDAwMDAwMDAwMDAxIiwidHlwZSI6InJlZnJlc2giLCJleHAiOjk5OTk5OTk5OTl9.test-refresh-signature";
    }

    public static String createExpiredAccessToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIwMDAwMDAwMDAwMDAwMDAxIiwiZW1haWwiOiJ0ZXN0QHVuaXZlcnNpdHkuYWMua3IiLCJyb2xlIjoiVVNFUiIsImV4cCI6MTYwMDAwMDAwMH0.expired-signature";
    }

    public static String createExpiredRefreshToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIwMDAwMDAwMDAwMDAwMDAxIiwidHlwZSI6InJlZnJlc2giLCJleHAiOjE2MDAwMDAwMDB9.expired-refresh-signature";
    }

    public static String createInvalidToken() {
        return "invalid.token.format";
    }

    public static String createTokenForUser(String userNo) {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiICsgdXNlck5vICsgIiIsImVtYWlsIjoidXNlckB1bml2ZXJzaXR5LmFjLmtyIiwicm9sZSI6IlVTRVIiLCJleHAiOjk5OTk5OTk5OTl9.user-specific-signature";
    }

    public static String createTokenWithEmail(String email) {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIwMDAwMDAwMDAwMDAwMDAxIiwiZW1haWwiOiIiICsgZW1haWwgKyAiIiwicm9sZSI6IlVTRVIiLCJleHAiOjk5OTk5OTk5OTl9.email-specific-signature";
    }

    public static Long createAccessTokenExpiry() {
        return 3600000L; // 1 hour in milliseconds
    }

    public static Long createRefreshTokenExpiry() {
        return 604800000L; // 7 days in milliseconds
    }
}
