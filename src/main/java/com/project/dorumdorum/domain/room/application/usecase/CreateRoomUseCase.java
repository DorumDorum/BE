package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.ALREADY_JOINED_USER;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRoomUseCase {

    private final UserService userService;
    private final RoomService roomService;
    private final RoommateService roommateService;
    private final RoomRuleService roomRuleService;
    private final RoomRuleMapper roomRuleMapper;

    /**
     * 방 생성
     * - 방 정보를 생성하고 생성자를 방장으로 등록
     * - 요청의 규칙 정보를 방 규칙으로 저장
     * - 생성된 방 번호를 반환
     */
    public String execute(String userNo, RoomCreateRequest request) {
        if (roommateService.existsByUserNo(userNo)) {
            throw new RestApiException(ALREADY_JOINED_USER);
        }
        Gender gender = userService.findById(userNo).getGender();
        Room room = roomService.create(userNo, gender, request);
        roommateService.create(userNo, room, RoomRole.HOST);

        RoomRule roomRule = roomRuleMapper.toRoomRule(room, request.rule());
        roomRuleService.save(roomRule);
        return room.getRoomNo();
    }
}
