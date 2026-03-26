package com.project.dorumdorum.domain.room.application.dto.request;

import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;

public record ChecklistFilterRequest(
        SortType sortType,
        String cursor,
        RoomType roomType,
        ResidencePeriod residencePeriod,
        Integer capacity,
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
        TrashCanType trashCan
) {
    public enum SortType {
        LATEST,
        REMAINING
    }
}
