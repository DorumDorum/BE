package com.project.dorumdorum.domain.room.unit.response;

import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import com.project.dorumdorum.domain.room.application.dto.response.MyRoomRuleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MyRoomRuleResponse Unit Tests")
class MyRoomRuleResponseTest {

    @Test
    @DisplayName("Should build response with all selected values")
    void builder_ShouldSetFields() {
        MyRoomRuleResponse response = MyRoomRuleResponse.builder()
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
                .otherNotes("note")
                .build();

        assertThat(response.bedtime()).isEqualTo("23:00");
        assertThat(response.wakeUp()).isEqualTo("07:00");
        assertThat(response.returnHomeTime()).isEqualTo("22:00");
        assertThat(response.lightsOutTime()).isEqualTo("00:00");
        assertThat(response.hairDryer()).isEqualTo("가능");
        assertThat(response.otherNotes()).isEqualTo("note");
        assertThat(response.returnHome()).isNotNull();
        assertThat(response.cleaning()).isNotNull();
        assertThat(response.trashCan()).isNotNull();
    }
}
