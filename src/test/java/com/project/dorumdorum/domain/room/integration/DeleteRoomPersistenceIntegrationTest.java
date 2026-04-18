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
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.testsupport.TestcontainersSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

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
    @Autowired private UserRepository userRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private RoommateRepository roommateRepository;
    @Autowired private RoomRequestRepository roomRequestRepository;
    @Autowired private RoomRuleRepository roomRuleRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatRoomMemberRepository chatRoomMemberRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;

    private static final String HOST_NO = "delete-host-001";
    private static final String APPLICANT_NO = "delete-applicant-001";

    private Room room;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .userNo(HOST_NO)
                .email("delete-host@gachon.ac.kr")
                .password("pw")
                .role(Role.USER)
                .studentNo("202400201")
                .name("DeleteHost")
                .nickname("DeleteHostNick")
                .gender(Gender.MALE)
                .build());
        userRepository.save(User.builder()
                .userNo(APPLICANT_NO)
                .email("delete-applicant@gachon.ac.kr")
                .password("pw")
                .role(Role.USER)
                .studentNo("202400202")
                .name("DeleteApplicant")
                .nickname("DeleteApplicantNick")
                .gender(Gender.MALE)
                .build());

        room = roomRepository.save(Room.builder()
                .roomType(RoomType.TYPE_1)
                .capacity(2)
                .title("delete-room")
                .hostUserNo(HOST_NO)
                .residencePeriod(ResidencePeriod.SEMESTER)
                .gender(Gender.MALE)
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
        chatMessageRepository.deleteAll();
        chatRoomMemberRepository.deleteAll();
        chatRoomRepository.deleteAll();
        roomRequestRepository.deleteAll();
        roomRuleRepository.deleteAll();
        roommateRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
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
    }
}
