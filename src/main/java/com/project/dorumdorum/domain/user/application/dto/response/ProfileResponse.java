package com.project.dorumdorum.domain.user.application.dto.response;

import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.User;

public record ProfileResponse(
        Long userNo,
        String name,
        String email,
        Gender gender
) {
    public static ProfileResponse create(User user) {
        return new ProfileResponse(
                user.getUserNo(),
                user.getName(),
                user.getEmail(),
                user.getGender()
        );
    }
}
