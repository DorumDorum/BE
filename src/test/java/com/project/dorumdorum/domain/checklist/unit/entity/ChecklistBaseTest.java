package com.project.dorumdorum.domain.checklist.unit.entity;

import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChecklistBase Unit Tests")
class ChecklistBaseTest {

    @Test
    @DisplayName("Should keep inherited checklist fields and update notes")
    void inheritedFieldsAndUpdateOtherNotes_Work() {
        UserChecklist checklist = UserChecklist.builder()
                .userNo("u1")
                .bedtime("23:00")
                .wakeUp("07:00")
                .returnHome(ReturnHomeType.values()[0])
                .returnHomeTime("22:00")
                .cleaning(CleaningType.values()[0])
                .phoneCall(PhoneCallType.values()[0])
                .sleepLight(SleepLightType.values()[0])
                .sleepHabit(SleepHabitType.values()[0])
                .snoring(SnoringType.values()[0])
                .showerTime(ShowerTimeType.values()[0])
                .eating(EatingType.values()[0])
                .lightsOut(LightsOutType.values()[0])
                .lightsOutTime("00:00")
                .homeVisit(HomeVisitType.values()[0])
                .smoking(SmokingType.values()[0])
                .refrigerator(RefrigeratorType.values()[0])
                .hairDryer("가능")
                .alarm(AlarmType.values()[0])
                .earphone(EarphoneType.values()[0])
                .keyskin(KeyskinType.values()[0])
                .heat(HeatType.values()[0])
                .cold(ColdType.values()[0])
                .study(StudyType.values()[0])
                .trashCan(TrashCanType.values()[0])
                .otherNotes("before")
                .build();

        assertThat(checklist.getBedtime()).isEqualTo("23:00");
        assertThat(checklist.getWakeUp()).isEqualTo("07:00");
        assertThat(checklist.getOtherNotes()).isEqualTo("before");

        checklist.updateOtherNotes("after");

        assertThat(checklist.getOtherNotes()).isEqualTo("after");
    }
}
