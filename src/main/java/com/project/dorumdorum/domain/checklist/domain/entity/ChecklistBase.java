package com.project.dorumdorum.domain.checklist.domain.entity;

import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public abstract class ChecklistBase {

    // 생활 패턴
    @Column(name = "bedtime", nullable = false)
    private String bedtime;

    @Column(name = "wake_up", nullable = false)
    private String wakeUp;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_home", nullable = false)
    private ReturnHomeType returnHome;

    @Column(name = "return_home_time", nullable = false)
    private String returnHomeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "cleaning", nullable = false)
    private CleaningType cleaning;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_call", nullable = false)
    private PhoneCallType phoneCall;

    @Enumerated(EnumType.STRING)
    @Column(name = "sleep_light", nullable = false)
    private SleepLightType sleepLight;

    @Enumerated(EnumType.STRING)
    @Column(name = "sleep_habit", nullable = false)
    private SleepHabitType sleepHabit;

    @Enumerated(EnumType.STRING)
    @Column(name = "snoring", nullable = false)
    private SnoringType snoring;

    @Enumerated(EnumType.STRING)
    @Column(name = "shower_time", nullable = false)
    private ShowerTimeType showerTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "eating", nullable = false)
    private EatingType eating;

    @Enumerated(EnumType.STRING)
    @Column(name = "lights_out", nullable = false)
    private LightsOutType lightsOut;

    @Column(name = "lights_out_time", nullable = false)
    private String lightsOutTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "home_visit", nullable = false)
    private HomeVisitType homeVisit;

    @Enumerated(EnumType.STRING)
    @Column(name = "smoking", nullable = false)
    private SmokingType smoking;

    @Enumerated(EnumType.STRING)
    @Column(name = "refrigerator", nullable = false)
    private RefrigeratorType refrigerator;

    // 추가 규칙
    @Column(name = "hair_dryer")
    private String hairDryer;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm")
    private AlarmType alarm;

    @Enumerated(EnumType.STRING)
    @Column(name = "earphone")
    private EarphoneType earphone;

    @Enumerated(EnumType.STRING)
    @Column(name = "keyskin")
    private KeyskinType keyskin;

    @Enumerated(EnumType.STRING)
    @Column(name = "heat")
    private HeatType heat;

    @Enumerated(EnumType.STRING)
    @Column(name = "cold")
    private ColdType cold;

    @Enumerated(EnumType.STRING)
    @Column(name = "study")
    private StudyType study;

    @Enumerated(EnumType.STRING)
    @Column(name = "trash_can")
    private TrashCanType trashCan;

    @Column(name = "other_notes", columnDefinition = "TEXT")
    private String otherNotes;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateChecklist(
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
    ) {
        this.bedtime = bedtime;
        this.wakeUp = wakeUp;
        this.returnHome = returnHome;
        this.returnHomeTime = returnHomeTime;
        this.cleaning = cleaning;
        this.phoneCall = phoneCall;
        this.sleepLight = sleepLight;
        this.sleepHabit = sleepHabit;
        this.snoring = snoring;
        this.showerTime = showerTime;
        this.eating = eating;
        this.lightsOut = lightsOut;
        this.lightsOutTime = lightsOutTime;
        this.homeVisit = homeVisit;
        this.smoking = smoking;
        this.refrigerator = refrigerator;
        this.hairDryer = hairDryer;
        this.alarm = alarm;
        this.earphone = earphone;
        this.keyskin = keyskin;
        this.heat = heat;
        this.cold = cold;
        this.study = study;
        this.trashCan = trashCan;
        this.otherNotes = otherNotes;
    }

    public void updateOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }
}
