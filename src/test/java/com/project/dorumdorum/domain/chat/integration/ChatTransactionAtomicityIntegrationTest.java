package com.project.dorumdorum.domain.chat.integration;

import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomMemberRepository;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomRepository;
import com.project.dorumdorum.domain.chat.domain.service.ChatMessageService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.room.application.usecase.KickRoommateUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateRepository;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 채팅 트랜잭션 원자성 통합 테스트.
 *
 * <p>검증 대상: {@code @TransactionalEventListener(BEFORE_COMMIT)} 리스너가
 * 부모 트랜잭션(KickRoommateUseCase)에 참여하여 아래 두 연산을 단일 트랜잭션으로 처리하는지 확인한다.
 * <ul>
 *   <li>방 도메인: roommateService.leaveRoom() — Roommate 삭제</li>
 *   <li>채팅 도메인: chatRoomMemberService.leave() — ChatRoomMember 삭제</li>
 * </ul>
 *
 * <p><b>주의:</b> 테스트 메서드에 {@code @Transactional}을 붙이면 부모 트랜잭션이 실제로 커밋되지 않아
 * BEFORE_COMMIT 이벤트가 발화되지 않는다. 따라서 의도적으로 {@code @Transactional}을 생략하고
 * {@code @AfterEach}에서 수동 cleanup을 수행한다.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("채팅 트랜잭션 원자성 통합 테스트 (BEFORE_COMMIT)")
class ChatTransactionAtomicityIntegrationTest {

    // ─── Firebase 목 (FileInputStream 오류 방지 — 테스트에서 FCM 사용 안 함) ──
    @MockBean @SuppressWarnings("unused") private FirebaseApp firebaseApp;
    @MockBean @SuppressWarnings("unused") private FirebaseMessaging firebaseMessaging;

    // ─── UseCase ──────────────────────────────────────────────────────────────
    @Autowired private KickRoommateUseCase kickRoommateUseCase;

    // ─── Repositories (DB 상태 직접 검증용) ─────────────────────────────────
    @Autowired private RoomRepository roomRepository;
    @Autowired private RoommateRepository roommateRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatRoomMemberRepository chatRoomMemberRepository;

    // ─── External Services (Mocking to isolate behavior) ─────────────────────
    @MockBean private SimpMessagingTemplate messagingTemplate;

    // ─── SpyBean (특정 메서드만 실패 강제 또는 외부 의존 우회) ─────────────
    @SpyBean private ChatRoomMemberService chatRoomMemberService;
    @SpyBean private UserService userService;
    @SpyBean private ChatMessageService chatMessageService;

    // ─── 테스트 픽스처 ────────────────────────────────────────────────────────
    private Room room;
    private ChatRoom chatRoom;

    /**
     * userNo에 FK 제약이 없으므로 실제 User 엔티티 없이 임의 ID 사용 가능.
     * '_atomicity_' 접두사로 다른 테스트 데이터와 충돌 방지.
     */
    private static final String HOST_NO   = "test_atomicity_host_001";
    private static final String MEMBER_NO = "test_atomicity_member_001";

    @BeforeEach
    void setUp() {
        // 1. Room 저장 (@PrePersist → currentMateCount=1, roomStatus=CONFIRM_PENDING 자동 설정)
        room = roomRepository.save(
                Room.builder()
                        .roomType(RoomType.TYPE_1)
                        .capacity(2)
                        .title("원자성 테스트 방")
                        .hostUserNo(HOST_NO)
                        .residencePeriod(ResidencePeriod.SEMESTER)
                        .build()
        );

        // 2. Roommate 저장 (HOST, MATE)
        //    @PrePersist가 confirmStatus를 PENDING으로 덮어쓰지만,
        //    KickRoommateUseCase는 RoomRole만 확인하므로 테스트에 영향 없음
        roommateRepository.save(
                Roommate.builder()
                        .room(room)
                        .userNo(HOST_NO)
                        .roomRole(RoomRole.HOST)
                        .confirmStatus(ConfirmStatus.COMPLETED)
                        .build()
        );
        roommateRepository.save(
                Roommate.builder()
                        .room(room)
                        .userNo(MEMBER_NO)
                        .roomRole(RoomRole.MEMBER)
                        .confirmStatus(ConfirmStatus.COMPLETED)
                        .build()
        );

        // 3. ChatRoom + ChatRoomMember 저장
        //    ChatRoom.builder()의 chatRoomType @Builder.Default = GROUP → findByRoomNo() 조회 가능
        chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .roomNo(room.getRoomNo())
                        .build()
        );
        chatRoomMemberRepository.save(
                ChatRoomMember.builder().chatRoom(chatRoom).userNo(HOST_NO).build()
        );
        chatRoomMemberRepository.save(
                ChatRoomMember.builder().chatRoom(chatRoom).userNo(MEMBER_NO).build()
        );

        // 각 테스트 시작 전 spy 상태 초기화
        Mockito.reset(chatRoomMemberService, userService, chatMessageService);
    }

    @AfterEach
    void tearDown() {
        // FK 순서: ChatRoomMember → ChatRoom → Roommate → Room
        chatRoomMemberRepository.deleteAll();
        chatRoomRepository.deleteAll();
        roommateRepository.deleteAll();
        roomRepository.deleteAll();
    }

    // =========================================================================
    // Test 1: 성공 경로 (원자적 커밋)
    // =========================================================================

    @Test
    @DisplayName("강퇴 성공: Roommate 삭제 + ChatRoomMember 삭제가 단일 트랜잭션으로 커밋된다")
    void kick_Success_RoommateAndChatMemberBothRemovedAtomically() {
        // given: 외부 의존 서비스 스텁 (User 엔티티 불필요, ChatMessage DB 저장 불필요)
        stubUserService();
        stubChatMessageService();

        // when
        kickRoommateUseCase.execute(HOST_NO, room.getRoomNo(), MEMBER_NO);

        // then-1: Roommate 삭제 확인 (방 도메인 변경)
        assertThat(roommateRepository.existsByUserNoAndRoomNo(MEMBER_NO, room.getRoomNo()))
                .as("강퇴된 멤버의 Roommate 레코드가 삭제되어야 한다")
                .isFalse();

        // then-2: ChatRoomMember 삭제 확인 (채팅 도메인 변경 — BEFORE_COMMIT 리스너에서 처리)
        assertThat(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, MEMBER_NO))
                .as("강퇴된 멤버의 ChatRoomMember 레코드가 같은 TX 내에서 삭제되어야 한다")
                .isFalse();
    }

    // =========================================================================
    // Test 2: 원자성 검증 (채팅 퇴장 실패 → 전체 롤백)
    // =========================================================================

    @Test
    @DisplayName("원자성: 채팅방 퇴장 실패 시 룸메이트 강퇴도 롤백된다 (BEFORE_COMMIT 단일 TX)")
    void kick_WhenChatRoomLeaveFails_RoommateKickAlsoRolledBack() {
        // given: BEFORE_COMMIT 리스너 내 chatRoomMemberService.leave() 강제 실패
        //        → 부모 TX(KickRoommateUseCase)에 예외가 전파되어 전체 롤백
        doThrow(new RuntimeException("채팅방 퇴장 DB 오류 (강제 실패)"))
                .when(chatRoomMemberService).leave(any());

        // when: 강퇴 실행 → BEFORE_COMMIT 예외 → 부모 TX 롤백
        assertThatThrownBy(() -> kickRoommateUseCase.execute(HOST_NO, room.getRoomNo(), MEMBER_NO))
                .isInstanceOf(RuntimeException.class);

        // then-1: Roommate 삭제가 롤백되어 여전히 존재해야 한다
        assertThat(roommateRepository.existsByUserNoAndRoomNo(MEMBER_NO, room.getRoomNo()))
                .as("BEFORE_COMMIT 실패로 TX 롤백 → Roommate 삭제 취소")
                .isTrue();

        // then-2: ChatRoomMember 변경 없이 그대로 존재해야 한다
        assertThat(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, MEMBER_NO))
                .as("TX 롤백으로 ChatRoomMember는 변경되지 않아야 한다")
                .isTrue();
    }

    // =========================================================================
    // Test 3: 채팅방 없는 경우 강퇴 — Roommate만 삭제, TX 정상 커밋
    // =========================================================================

    @Test
    @DisplayName("채팅방 없는 경우: Roommate만 삭제되고 TX가 정상 커밋된다")
    void kick_WhenNoChatRoom_OnlyRoommateRemovedAndTxCommits() {
        // given: ChatRoomMember + ChatRoom 제거 → 채팅방이 없는 상태 시뮬레이션
        chatRoomMemberRepository.deleteAll();
        chatRoomRepository.deleteAll();
        stubUserService();
        stubChatMessageService();

        // when: 채팅방 없어도 강퇴 성공해야 한다 (채팅 이벤트 리스너가 gracefully 처리)
        kickRoommateUseCase.execute(HOST_NO, room.getRoomNo(), MEMBER_NO);

        // then: Roommate 삭제 확인
        assertThat(roommateRepository.existsByUserNoAndRoomNo(MEMBER_NO, room.getRoomNo()))
                .as("채팅방이 없어도 Roommate는 삭제되어야 한다")
                .isFalse();
    }

    // =========================================================================
    // Test 4: chatMessageService.save() 실패 → 전체 TX 롤백
    // =========================================================================

    @Test
    @DisplayName("원자성: chatMessageService.save() 실패 시 Roommate 강퇴도 롤백된다")
    void kick_WhenChatMessageSaveFails_RoommateKickAlsoRolledBack() {
        // given: BEFORE_COMMIT 리스너 내 chatMessageService.save() 강제 실패
        stubUserService();
        doThrow(new RuntimeException("메시지 저장 DB 오류 (강제 실패)"))
                .when(chatMessageService).save(any(), anyString(), anyString(), any(), anyInt());

        // when
        assertThatThrownBy(() -> kickRoommateUseCase.execute(HOST_NO, room.getRoomNo(), MEMBER_NO))
                .isInstanceOf(RuntimeException.class);

        // then-1: Roommate 삭제가 롤백되어 여전히 존재해야 한다
        assertThat(roommateRepository.existsByUserNoAndRoomNo(MEMBER_NO, room.getRoomNo()))
                .as("chatMessageService 실패로 TX 롤백 → Roommate 삭제 취소")
                .isTrue();

        // then-2: ChatRoomMember 변경 없이 그대로 존재해야 한다
        assertThat(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, MEMBER_NO))
                .as("TX 롤백으로 ChatRoomMember는 변경되지 않아야 한다")
                .isTrue();
    }

    // =========================================================================
    // Test 5: WebSocket 브로드캐스트 실패 (P1 케이스: 외부 요인 예외 격리)
    // =========================================================================

    @Test
    @DisplayName("P1 케이스: WebSocket 메시지 전송 실패 시 TX는 롤백되지 않고 정상 커밋된다 (디커플링 보장)")
    void kick_WhenWebSocketBroadcastFails_TxCommitsSuccessfully() {
        // given: 메시지 전송 시 강제 예외 발생
        stubUserService();
        stubChatMessageService();
        doThrow(new RuntimeException("WebSocket 전송 실패 (강제 에러)"))
                .when(messagingTemplate).convertAndSend(anyString(), any(Object.class));

        // when: 강퇴 실행
        kickRoommateUseCase.execute(HOST_NO, room.getRoomNo(), MEMBER_NO);

        // then: Roommate 및 ChatRoomMember는 정상 삭제되어야 한다 (TX 커밋)
        assertThat(roommateRepository.existsByUserNoAndRoomNo(MEMBER_NO, room.getRoomNo()))
                .as("WebSocket 실패가 DB 트랜잭션 롤백으로 이어지지 않아야 한다")
                .isFalse();
        assertThat(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, MEMBER_NO))
                .as("채팅방 멤버 레코드도 삭제된 상태로 커밋되어야 한다")
                .isFalse();
    }

    // =========================================================================
    // Test 6: 사용자가 채팅방에 이미 없는 경우 (P1 케이스: 멱등성 보장)
    // =========================================================================

    @Test
    @DisplayName("P1 케이스: 강퇴 대상이 이미 채팅방 멤버가 아니어도 강퇴 트랜잭션은 성공한다")
    void kick_WhenAlreadyLeftChatRoom_TxCommitsSuccessfully() {
        // given: 예외적으로 ChatRoomMember 레코드만 미리 삭제된 상태
        chatRoomMemberService.leave(
                chatRoomMemberRepository.findByChatRoomAndUserNo(chatRoom, MEMBER_NO)
                        .orElseThrow()
        );
        chatRoomMemberRepository.flush(); // 영속성 컨텍스트 비우기
        stubUserService();
        stubChatMessageService();

        // when: 방 강퇴
        kickRoommateUseCase.execute(HOST_NO, room.getRoomNo(), MEMBER_NO);

        // then: 트랜잭션 정상 수행됨 (방에서는 삭제됨)
        assertThat(roommateRepository.existsByUserNoAndRoomNo(MEMBER_NO, room.getRoomNo()))
                .isFalse();
    }

    // =========================================================================
    // 헬퍼
    // =========================================================================

    /** RoommateKickedEventListener 내 userService.findById() 우회 (User 엔티티 저장 불필요) */
    private void stubUserService() {
        User fakeUser = User.builder()
                .nickname("테스트멤버")
                .name("홍길동")
                .email("member-atomicity@test.com")
                .password("pw")
                .role(Role.USER)
                .studentNo("202400001")
                .build();
        doReturn(fakeUser).when(userService).findById(MEMBER_NO);
    }

    /** chatMessageService.save() 우회 — ChatMessage DB 저장 없이 테스트 (cleanup 단순화) */
    private void stubChatMessageService() {
        ChatMessage fakeMessage = mock(ChatMessage.class);
        when(fakeMessage.getMessageNo()).thenReturn("fake-msg-001");
        when(fakeMessage.getCreatedAt()).thenReturn(LocalDateTime.now());
        doReturn(fakeMessage)
                .when(chatMessageService)
                .save(any(), anyString(), anyString(), any(), anyInt());
    }
}
