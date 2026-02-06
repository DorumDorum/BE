package com.project.dorumdorum.domain.checklist.application.dto.response;

import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import lombok.Builder;

@Builder
public record MyRoomRuleResponse(
        String bedtime,
        String wakeUp,
        ReturnHomeType returnHome,
        String returnHomeTime,
        CleaningType cleaning,
        PhoneCallType phoneCall,
        SleepLightType sleepLight,
        SleepHabitType sleepHabit,
        SnoringType snoring,
        ShowerTimeType showerTime,
        EatingType eating,
        LightsOutType lightsOut,
        String lightsOutTime,
        HomeVisitType homeVisit,
        SmokingType smoking,
        RefrigeratorType refrigerator,
        String hairDryer,
        AlarmType alarm,
        EarphoneType earphone,
        KeyskinType keyskin,
        HeatType heat,
        ColdType cold,
        StudyType study,
        TrashCanType trashCan,
        String otherNotes
) {}
