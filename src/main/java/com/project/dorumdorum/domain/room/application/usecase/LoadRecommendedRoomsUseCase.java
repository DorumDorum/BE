package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.checklist.domain.entity.ChecklistBase;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.domain.repository.RoomRuleRepository;
import com.project.dorumdorum.domain.checklist.domain.repository.UserChecklistRepository;
import com.project.dorumdorum.domain.checklist.domain.service.UserChecklistService;
import com.project.dorumdorum.domain.room.application.dto.response.RecommendedRoomResponse;
import com.project.dorumdorum.domain.room.application.dto.response.RecommendedRoomsPageResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadRecommendedRoomsUseCase {

    private static final double STRONG_MATCH_THRESHOLD = 0.85;
    private static final double MATCH_THRESHOLD = 0.65;

    private final UserChecklistService userChecklistService;
    private final UserChecklistRepository userChecklistRepository;
    private final RoomRuleRepository roomRuleRepository;
    private final UserService userService;

    public RecommendedRoomsPageResponse execute(String userNo) {
        if (!userChecklistRepository.existsByUserNo(userNo)) {
            return new RecommendedRoomsPageResponse(false, List.of());
        }

        UserChecklist my = userChecklistService.findByUserNo(userNo);
        List<RoomRule> roomRules = roomRuleRepository.findAllActiveWithRoom();

        List<RecommendedRoomResponse> items = roomRules.stream()
                .map(rr -> buildResponse(my, rr))
                .filter(r -> r != null)
                .sorted(Comparator.comparingDouble(r -> -(double) r.matchedCount() / Math.max(r.totalCount(), 1)))
                .toList();

        return new RecommendedRoomsPageResponse(true, items);
    }

    private RecommendedRoomResponse buildResponse(UserChecklist my, RoomRule rr) {
        List<FieldResult> fields = compareFields(my, rr);

        int totalCount = (int) fields.stream().filter(FieldResult::filled).count();
        if (totalCount == 0) return null;

        int matchedCount = (int) fields.stream().filter(f -> f.filled() && f.match()).count();
        double score = (double) matchedCount / totalCount;

        String matchLabel;
        if (score >= STRONG_MATCH_THRESHOLD) matchLabel = "잘 맞아요";
        else if (score >= MATCH_THRESHOLD) matchLabel = "괜찮아요";
        else return null;

        List<String> highlights = fields.stream()
                .filter(f -> f.filled() && f.match())
                .limit(3)
                .map(FieldResult::label)
                .toList();

        Room room = rr.getRoom();
        String hostNickname = userService.findById(room.getHostUserNo()).getNickname();

        return RecommendedRoomResponse.builder()
                .roomNo(room.getRoomNo())
                .title(room.getTitle())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .currentMateCount(room.getCurrentMateCount())
                .remaining(room.getRemaining())
                .roomStatus(room.getRoomStatus())
                .residencePeriod(room.getResidencePeriod().name())
                .hostNickname(hostNickname)
                .matchLabel(matchLabel)
                .matchedCount(matchedCount)
                .totalCount(totalCount)
                .highlights(highlights)
                .build();
    }

    private List<FieldResult> compareFields(UserChecklist my, ChecklistBase room) {
        List<FieldResult> results = new ArrayList<>();
        addField(results, "취침", my.getBedtime(), room.getBedtime());
        addField(results, "기상", my.getWakeUp(), room.getWakeUp());
        addField(results, "귀가", my.getReturnHome(), room.getReturnHome());
        addField(results, "청소", my.getCleaning(), room.getCleaning());
        addField(results, "방에서 전화", my.getPhoneCall(), room.getPhoneCall());
        addField(results, "잠귀", my.getSleepLight(), room.getSleepLight());
        addField(results, "잠버릇", my.getSleepHabit(), room.getSleepHabit());
        addField(results, "코골이", my.getSnoring(), room.getSnoring());
        addField(results, "샤워 시간", my.getShowerTime(), room.getShowerTime());
        addField(results, "방에서 취식", my.getEating(), room.getEating());
        addField(results, "소등", my.getLightsOut(), room.getLightsOut());
        addField(results, "본가 주기", my.getHomeVisit(), room.getHomeVisit());
        addField(results, "흡연", my.getSmoking(), room.getSmoking());
        addField(results, "냉장고", my.getRefrigerator(), room.getRefrigerator());
        addField(results, "알람", my.getAlarm(), room.getAlarm());
        addField(results, "이어폰", my.getEarphone(), room.getEarphone());
        addField(results, "키스킨", my.getKeyskin(), room.getKeyskin());
        addField(results, "더위", my.getHeat(), room.getHeat());
        addField(results, "추위", my.getCold(), room.getCold());
        addField(results, "공부", my.getStudy(), room.getStudy());
        addField(results, "쓰레기통", my.getTrashCan(), room.getTrashCan());
        return results;
    }

    private void addField(List<FieldResult> results, String label, Object myValue, Object roomValue) {
        boolean filled = myValue != null && !(myValue instanceof String s && s.isBlank());
        boolean match = filled && Objects.equals(myValue, roomValue);
        results.add(new FieldResult(label, filled, match));
    }

    private record FieldResult(String label, boolean filled, boolean match) {}
}
