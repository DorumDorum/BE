package com.project.dorumdorum.domain.room.integration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.repository.ChatMessageRepository;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomMemberRepository;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomRepository;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.checklist.domain.repository.RoomRuleRepository;
import com.project.dorumdorum.domain.room.application.usecase.DeleteRoomUseCase;
import com.project.dorumdorum.domain.room.application.usecase.KickRoommateUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomLikeRepository;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestRepository;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateRepository;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 명시적 flush() 제거 후 실제 DB 반영 검증 통합 테스트.
 *
 * 검증 대상:
 * 1. KickRoommateUseCase — room.minusCurrentMate()가 flushAutomatically(BEFORE_COMMIT 리스너
 *    내 decreaseUnreadCount)를 통해 DB에 반영되는지
 * 2. DeleteRoomUseCase — room.delete()가 deleteAllByRoom(flushAutomatically=true)를 통해
 *    DB에 반영되고 연관 데이터가 정리되는지
 *
 * @ActiveProfiles("local-db"): 로컬 Docker PostgreSQL(dorumdorum_test DB)에 직접 연결.
 * Testcontainers 없이 실제 DB로 동작을 검증한다.
 */
@SpringBootTest(properties = {
        "spring.datasource.driver-class-name=org.postgresql.Driver",
        "spring.datasource.url=jdbc:postgresql://localhost:5432/dorumdorum_test",
        "spring.datasource.username=dorumdorum",
        "spring.datasource.password=test1234",
        "spring.jpa.defer-datasource-initialization=true",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=always",
        "spring.sql.init.schema-locations=classpath:schema.sql"
})
@ActiveProfiles("test")
@DisplayName("flush() 제거 후 실제 DB 반영 통합 테스트")
class FlushRemovalIntegrationTest {

    @MockitoBean private FirebaseApp firebaseApp;
    @MockitoBean private FirebaseMessaging firebaseMessaging;
    @MockitoBean private SimpMessagingTemplate messagingTemplate;

    @MockitoSpyBean private UserService userService;
    @MockitoSpyBean private ChatMessageService chatMessageService;
    @MockitoSpyBean private ChatRoomMemberService chatRoomMemberService;

    @Autowired private KickRoommateUseCase kickRoommateUseCase;
    @Autowired private DeleteRoomUseCase deleteRoomUseCase;

    @Autowired private RoomRepository roomRepository;
    @Autowired private RoommateRepository roommateRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatRoomMemberRepository chatRoomMemberRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private RoomRequestRepository roomRequestRepository;
    @Autowired private RoomRuleRepository roomRuleRepository;
    @Autowired private RoomLikeRepository roomLikeRepository;

    private static final String KICK_HOST_NO   = "flush_test_kick_host_001";
    private static final String KICK_MEMBER_NO = "flush_test_kick_member_001";
    private static final String DELETE_HOST_NO = "flush_test_del_host_001";

    private Room kickRoom;
    private ChatRoom kickChatRoom;
    private Room deleteRoom;

    @BeforeEach
    void setUp() {
        // ── KickRoommateUseCase 픽스처 ────────────────────────────────────────
        kickRoom = roomRepository.save(Room.builder()
                .roomType(RoomType.TYPE_1).capacity(2).title("kick-flush-test")
                .hostUserNo(KICK_HOST_NO).residencePeriod(ResidencePeriod.SEMESTER)
                .gender(Gender.MALE).build());
        kickRoom.plusCurrentMate();
        kickRoom = roomRepository.saveAndFlush(kickRoom);

        roommateRepository.save(Roommate.builder().room(kickRoom).userNo(KICK_HOST_NO)
                .roomRole(RoomRole.HOST).confirmStatus(ConfirmStatus.ACCEPTED).build());
        roommateRepository.save(Roommate.builder().room(kickRoom).userNo(KICK_MEMBER_NO)
                .roomRole(RoomRole.MEMBER).confirmStatus(ConfirmStatus.ACCEPTED).build());

        kickChatRoom = chatRoomRepository.save(ChatRoom.builder().roomNo(kickRoom.getRoomNo()).build());
        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(kickChatRoom).userNo(KICK_HOST_NO).build());
        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(kickChatRoom).userNo(KICK_MEMBER_NO).build());

        // ── DeleteRoomUseCase 픽스처 ─────────────────────────────────────────
        deleteRoom = roomRepository.save(Room.builder()
                .roomType(RoomType.TYPE_1).capacity(2).title("delete-flush-test")
                .hostUserNo(DELETE_HOST_NO).residencePeriod(ResidencePeriod.SEMESTER)
                .gender(Gender.MALE).build());

        roommateRepository.save(Roommate.builder().room(deleteRoom).userNo(DELETE_HOST_NO)
                .roomRole(RoomRole.HOST).confirmStatus(ConfirmStatus.ACCEPTED).build());

        stubUserService();
        stubChatMessageService();
    }

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAllInBatch();
        chatRoomMemberRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        roomRequestRepository.deleteAllInBatch();
        roomRuleRepository.deleteAllInBatch();
        roomLikeRepository.deleteAllInBatch();
        roommateRepository.deleteAllInBatch();
        roomRepository.deleteAllInBatch();
    }

    // =========================================================================
    // KickRoommateUseCase: room.minusCurrentMate() flush 검증
    // =========================================================================

    @Test
    @DisplayName("강퇴 후 room.currentMateCount 감소가 DB에 반영된다 (explicit flush 없이)")
    void kick_CurrentMateCountDecrement_PersistedWithoutExplicitFlush() {
        int beforeCount = roomRepository.findById(kickRoom.getRoomNo()).orElseThrow().getCurrentMateCount();

        kickRoommateUseCase.execute(KICK_HOST_NO, kickRoom.getRoomNo(), KICK_MEMBER_NO);

        Room persisted = roomRepository.findById(kickRoom.getRoomNo()).orElseThrow();
        assertThat(persisted.getCurrentMateCount())
                .as("minusCurrentMate()가 flush 없이도 DB에 반영되어야 한다")
                .isEqualTo(beforeCount - 1);
    }

    @Test
    @DisplayName("강퇴 후 Roommate 삭제가 DB에 반영된다 (explicit flush 없이)")
    void kick_RoommateDelete_PersistedWithoutExplicitFlush() {
        kickRoommateUseCase.execute(KICK_HOST_NO, kickRoom.getRoomNo(), KICK_MEMBER_NO);

        assertThat(roommateRepository.existsByUserNoAndRoomNo(KICK_MEMBER_NO, kickRoom.getRoomNo()))
                .as("Roommate 삭제가 DB에 반영되어야 한다")
                .isFalse();
    }

    // =========================================================================
    // DeleteRoomUseCase: room.delete() (deletedAt) flush 검증
    // =========================================================================

    @Test
    @DisplayName("방 삭제 후 room.deletedAt이 DB에 반영된다 (explicit flush 없이)")
    void delete_SoftDeletedAt_PersistedWithoutExplicitFlush() {
        deleteRoomUseCase.execute(DELETE_HOST_NO, deleteRoom.getRoomNo());

        Room persisted = roomRepository.findById(deleteRoom.getRoomNo()).orElseThrow();
        assertThat(persisted.getDeletedAt())
                .as("room.delete()가 flush 없이도 deletedAt이 DB에 반영되어야 한다")
                .isNotNull();
    }

    @Test
    @DisplayName("방 삭제 후 Roommate가 DB에서 제거된다 (explicit flush 없이)")
    void delete_RoommateCleanup_PersistedWithoutExplicitFlush() {
        deleteRoomUseCase.execute(DELETE_HOST_NO, deleteRoom.getRoomNo());

        assertThat(roommateRepository.findByUserNo(DELETE_HOST_NO))
                .as("방 삭제 시 Roommate가 DB에서 제거되어야 한다")
                .isEmpty();
    }

    // =========================================================================
    // 헬퍼
    // =========================================================================

    private void stubUserService() {
        User fakeUser = User.builder()
                .nickname("테스트멤버").name("홍길동").email("flush-test@test.com")
                .password("pw").role(Role.USER).studentNo("202499999").build();
        doReturn(fakeUser).when(userService).findById(KICK_MEMBER_NO);
    }

    private void stubChatMessageService() {
        var fakeMessage = mock(com.project.dorumdorum.domain.chat.domain.entity.ChatMessage.class);
        when(fakeMessage.getMessageNo()).thenReturn("fake-flush-msg-001");
        when(fakeMessage.getCreatedAt()).thenReturn(java.time.LocalDateTime.now());
        doReturn(fakeMessage).when(chatMessageService).save(any(), anyString(), anyString(), any(), anyInt());
    }
}
