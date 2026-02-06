package com.project.dorumdorum.domain.checklist.application.dto.request;

import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRoomRuleRequest(
        @NotBlank String bedtime,
        @NotBlank String wakeUp,
        @NotNull ReturnHomeType returnHome,
        @NotBlank String returnHomeTime,
        @NotNull CleaningType cleaning,
        @NotNull PhoneCallType phoneCall,
        @NotNull SleepLightType sleepLight,
        @NotNull SleepHabitType sleepHabit,
        @NotNull SnoringType snoring,
        @NotNull ShowerTimeType showerTime,
        @NotNull EatingType eating,
        @NotNull LightsOutType lightsOut,
        @NotBlank String lightsOutTime,
        @NotNull HomeVisitType homeVisit,
        @NotNull SmokingType smoking,
        @NotNull RefrigeratorType refrigerator,
        String hairDryer,
        AlarmType alarm,
        EarphoneType earphone,
        KeyskinType keyskin,
        HeatType heat,
        ColdType cold,
        StudyType study,
        TrashCanType trashCan,
        String otherNotes,
        @NotNull RoomType roomType,
        @NotNull Integer capacity,
        @NotNull ResidencePeriod residencePeriod
) {}
