package com.project.dorumdorum.domain.room.application.dto.request;

import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import jakarta.validation.constraints.NotNull;

public record ChecklistFilterRequest(
        @NotNull SortType sortType,
        String cursor,
        String keyword,
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
    public ChecklistFilterRequest(
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
        this(
                sortType,
                cursor,
                null,
                roomType,
                residencePeriod,
                capacity,
                bedtime,
                wakeUp,
                returnHome,
                returnHomeTime,
                cleaning,
                phoneCall,
                sleepLight,
                sleepHabit,
                snoring,
                showerTime,
                eating,
                lightsOut,
                lightsOutTime,
                homeVisit,
                smoking,
                refrigerator,
                hairDryer,
                alarm,
                earphone,
                keyskin,
                heat,
                cold,
                study,
                trashCan
        );
    }

    public enum SortType {
        LATEST,
        REMAINING
    }
}
