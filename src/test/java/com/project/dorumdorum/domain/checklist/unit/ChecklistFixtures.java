package com.project.dorumdorum.domain.checklist.unit;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;

public final class ChecklistFixtures {

    private ChecklistFixtures() {
    }

    public static CreateUserChecklistRequest createUserChecklistRequest() {
        return new CreateUserChecklistRequest(
                "23:00", "07:00", ReturnHomeType.values()[0], "22:00",
                CleaningType.values()[0], PhoneCallType.values()[0], SleepLightType.values()[0],
                SleepHabitType.values()[0], SnoringType.values()[0], ShowerTimeType.values()[0],
                EatingType.values()[0], LightsOutType.values()[0], "00:00",
                HomeVisitType.values()[0], SmokingType.values()[0], RefrigeratorType.values()[0],
                "가능", AlarmType.values()[0], EarphoneType.values()[0], KeyskinType.values()[0],
                HeatType.values()[0], ColdType.values()[0], StudyType.values()[0],
                TrashCanType.values()[0], "note"
        );
    }

    public static UpdateUserChecklistRequest updateUserChecklistRequest() {
        return new UpdateUserChecklistRequest(
                "23:30", "07:30", ReturnHomeType.values()[0], "23:00",
                CleaningType.values()[0], PhoneCallType.values()[0], SleepLightType.values()[0],
                SleepHabitType.values()[0], SnoringType.values()[0], ShowerTimeType.values()[0],
                EatingType.values()[0], LightsOutType.values()[0], "00:30",
                HomeVisitType.values()[0], SmokingType.values()[0], RefrigeratorType.values()[0],
                "불가", AlarmType.values()[0], EarphoneType.values()[0], KeyskinType.values()[0],
                HeatType.values()[0], ColdType.values()[0], StudyType.values()[0],
                TrashCanType.values()[0], "updated-note"
        );
    }

    public static CreateRoomRuleRequest createRoomRuleRequest() {
        return new CreateRoomRuleRequest(
                "23:00", "07:00", ReturnHomeType.values()[0], "22:00",
                CleaningType.values()[0], PhoneCallType.values()[0], SleepLightType.values()[0],
                SleepHabitType.values()[0], SnoringType.values()[0], ShowerTimeType.values()[0],
                EatingType.values()[0], LightsOutType.values()[0], "00:00",
                HomeVisitType.values()[0], SmokingType.values()[0], RefrigeratorType.values()[0],
                "가능", AlarmType.values()[0], EarphoneType.values()[0], KeyskinType.values()[0],
                HeatType.values()[0], ColdType.values()[0], StudyType.values()[0],
                TrashCanType.values()[0], "room-note"
        );
    }

    public static UpdateRoomRuleRequest updateRoomRuleRequest() {
        return new UpdateRoomRuleRequest(
                "22:00", "06:00", ReturnHomeType.values()[0], "21:00",
                CleaningType.values()[0], PhoneCallType.values()[0], SleepLightType.values()[0],
                SleepHabitType.values()[0], SnoringType.values()[0], ShowerTimeType.values()[0],
                EatingType.values()[0], LightsOutType.values()[0], "23:30",
                HomeVisitType.values()[0], SmokingType.values()[0], RefrigeratorType.values()[0],
                "가능", AlarmType.values()[0], EarphoneType.values()[0], KeyskinType.values()[0],
                HeatType.values()[0], ColdType.values()[0], StudyType.values()[0],
                TrashCanType.values()[0], "updated-room-note",
                RoomType.TYPE_2, 3, ResidencePeriod.HALF_YEAR
        );
    }
}
