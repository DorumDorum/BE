package com.project.dorumdorum.domain.user.application.dto.request;

import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.global.exception.RestApiException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.AGE_PARSING_ERROR;

public record SignUpRequest(
        @NotBlank String name,
        @NotBlank String nickname,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String passwordCheck,
        @NotNull Gender gender,
        @NotBlank String studentNo,
        @NotBlank String major,
        @NotBlank String grade,
        @NotBlank String birth,
        @NotNull CreateUserChecklistRequest checklist
) {
    public boolean isCheckedPassword() {
        return password().equals(passwordCheck());
    }

    public int calculateAge() {
        try {
            LocalDate birthDate = LocalDate.parse(birth, DateTimeFormatter.ISO_DATE);
            LocalDate now = LocalDate.now();
            return now.getYear() - birthDate.getYear() + 1;
        } catch (Exception e) {
            throw new RestApiException(AGE_PARSING_ERROR);
        }
    }
}
