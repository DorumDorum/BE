package com.project.dorumdorum.domain.room.integration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomType;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.repository.ChatMessageRepository;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomMemberRepository;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomRepository;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.entity.enums.*;
import com.project.dorumdorum.domain.checklist.domain.repository.RoomRuleRepository;
import com.project.dorumdorum.domain.room.application.usecase.DeleteRoomUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestRepository;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateRepository;
import com.project.dorumdorum.testsupport.TestcontainersSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * userNo에 FK 제약이 없으므로 실제 User 엔티티 없이 임의 ID 사용 가능.
 * '_delete_room_' 접두사로 다른 테스트 데이터와 충돌 방지.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("DeleteRoom 영속성 통합 테스트")
class DeleteRoomPersistenceIntegrationTest {

    @BeforeAll
    static void requireDocker() {
        Assumptions.assumeTrue(
                TestcontainersSupport.requireDockerOrSkip("DeleteRoomPersistenceIntegrationTest"),
                "Docker is required for DeleteRoomPersistenceIntegrationTest"
        );
    }

    @MockitoBean private FirebaseApp firebaseApp;
    @MockitoBean private FirebaseMessaging firebaseMessaging;
    @MockitoBean private SimpMessagingTemplate messagingTemplate;

    @Autowired private DeleteRoomUseCase deleteRoomUseCase;
    @Autowired private RoomRepository roomRepository;
    @Autowired private RoommateRepository roommateRepository;
    @Autowired private RoomRequestRepository roomRequestRepository;
    @Autowired private RoomRuleRepository roomRuleRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatRoomMemberRepository chatRoomMemberRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private JdbcTemplate jdbcTemplate;

    private static final String HOST_NO      = "test_delete_room_host_001";
    private static final String APPLICANT_NO = "test_delete_room_applicant_001";

    private Room room;

    @BeforeEach
    void setUp() {
        truncateTables();

        room = roomRepository.save(Room.builder()
                .roomType(RoomType.TYPE_1)
                .capacity(2)
                .title("delete-room")
                .hostUserNo(HOST_NO)
                .residencePeriod(ResidencePeriod.SEMESTER)
                .gender(com.project.dorumdorum.domain.user.domain.entity.Gender.MALE)
                .build());

        roommateRepository.save(Roommate.builder()
                .room(room)
                .userNo(HOST_NO)
                .roomRole(RoomRole.HOST)
                .confirmStatus(ConfirmStatus.PENDING)
                .build());

        roomRuleRepository.save(RoomRule.builder()
                .room(room)
                .bedtime("23:30")
                .wakeUp("07:30")
                .returnHome(ReturnHomeType.FLEXIBLE)
                .returnHomeTime("22:00")
                .cleaning(CleaningType.REGULAR)
                .phoneCall(PhoneCallType.ALLOWED)
                .sleepLight(SleepLightType.DARK)
                .sleepHabit(SleepHabitType.MILD)
                .snoring(SnoringType.MILD_OR_NONE)
                .showerTime(ShowerTimeType.EVENING)
                .eating(EatingType.ALLOWED_WITH_VENTILATION)
                .lightsOut(LightsOutType.AFTER_TIME)
                .lightsOutTime("00:30")
                .homeVisit(HomeVisitType.MONTHLY_OR_MORE)
                .smoking(SmokingType.NON_SMOKER)
                .refrigerator(RefrigeratorType.DECIDE_AFTER_DISCUSSION)
                .hairDryer("FLEXIBLE")
                .alarm(AlarmType.VIBRATION)
                .earphone(EarphoneType.FLEXIBLE)
                .keyskin(KeyskinType.FLEXIBLE)
                .heat(HeatType.MODERATE)
                .cold(ColdType.MODERATE)
                .study(StudyType.FLEXIBLE)
                .trashCan(TrashCanType.SHARED)
                .otherNotes("delete test")
                .build());

        roomRequestRepository.save(RoomRequest.builder()
                .room(room)
                .userNo(APPLICANT_NO)
                .direction(Direction.USER_TO_ROOM)
                .introduction("please")
                .additionalMessage("delete me")
                .build());

        ChatRoom directChatRoom = chatRoomRepository.save(ChatRoom.builder()
                .roomNo(room.getRoomNo())
                .chatRoomType(ChatRoomType.DIRECT)
                .applicantUserNo(APPLICANT_NO)
                .build());
        ChatRoom groupChatRoom = chatRoomRepository.save(ChatRoom.builder()
                .roomNo(room.getRoomNo())
                .chatRoomType(ChatRoomType.GROUP)
                .build());
        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(directChatRoom).userNo(HOST_NO).build());
        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(directChatRoom).userNo(APPLICANT_NO).build());
        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(groupChatRoom).userNo(HOST_NO).build());
        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(directChatRoom)
                .senderNo(HOST_NO)
                .content("hello")
                .messageType(MessageType.TEXT)
                .unreadCount(1)
                .build());
        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(groupChatRoom)
                .senderNo(HOST_NO)
                .content("group hello")
                .messageType(MessageType.TEXT)
                .unreadCount(0)
                .build());
    }

    @AfterEach
    void tearDown() {
        truncateTables();
    }

    @Test
    @DisplayName("삭제 성공 시 room은 soft delete 되고 연관 요청/규칙/채팅은 정리된다")
    void execute_PersistsSoftDeleteAndCleanup() {
        deleteRoomUseCase.execute(HOST_NO, room.getRoomNo());

        Room persistedRoom = roomRepository.findById(room.getRoomNo()).orElseThrow();

        assertThat(persistedRoom.getDeletedAt()).isNotNull();
        assertThat(roommateRepository.findByUserNo(HOST_NO)).isEmpty();
        assertThat(roomRequestRepository.findAll()).isEmpty();
        assertThat(roomRuleRepository.findByRoomNo(room.getRoomNo())).isEmpty();
        assertThat(chatRoomRepository.findAllByRoomNo(room.getRoomNo())).isEmpty();
        assertThat(chatRoomMemberRepository.findAll()).isEmpty();
        assertThat(chatMessageRepository.findAll()).isEmpty();

        await().untilAsserted(() ->
                verify(messagingTemplate, times(3))
                        .convertAndSendToUser(any(), eq("/queue/notification"), any()));
    }

    private void truncateTables() {
        jdbcTemplate.execute("""
                TRUNCATE TABLE
                    chat_message,
                    chat_room_member,
                    chat_room,
                    room_request,
                    room_rule,
                    room_like,
                    roommate,
                    room
                RESTART IDENTITY CASCADE
                """);
    }
}
