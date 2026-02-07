package com.project.dorumdorum.domain.user.application.dto.response;

import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.User;

public record ProfileResponse(
        String userNo,
        String nickname,
        String name,
        String email,
        Gender gender,
        String studentNo,
        String major,
        String grade,
        String birth,
        Integer age
) {
    public static ProfileResponse create(User user) {
        return new ProfileResponse(
                user.getUserNo(),
                user.getNickname(),
                user.getName(),
                user.getEmail(),
                user.getGender(),
                user.getStudentNo(),
                user.getMajor(),
                user.getGrade(),
                user.getBirth(),
                user.getAge()
        );
    }
}
