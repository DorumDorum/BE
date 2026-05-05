package com.project.dorumdorum.domain.room.integration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.dorumdorum.domain.chat.domain.entity.ChatMessage;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import com.project.dorumdorum.domain.chat.domain.repository.ChatMessageRepository;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomMemberRepository;
import com.project.dorumdorum.domain.chat.domain.repository.ChatRoomRepository;
import com.project.dorumdorum.domain.room.application.usecase.KickRoommateUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateRepository;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDateTime;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * userNo에 FK 제약이 없으므로 실제 User 엔티티 없이 임의 ID 사용 가능.
 * RoommateKickedEventListener 내 userService.findById() 호출은 @MockitoSpyBean으로 우회.
 * '_kick_roommate_' 접두사로 다른 테스트 데이터와 충돌 방지.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("KickRoommate 영속성 통합 테스트")
class KickRoommatePersistenceIntegrationTest {

    @BeforeAll
    static void requireDocker() {
        Assumptions.assumeTrue(
                TestcontainersSupport.requireDockerOrSkip("KickRoommatePersistenceIntegrationTest"),
                "Docker is required for KickRoommatePersistenceIntegrationTest"
        );
    }

    @MockitoBean private FirebaseApp firebaseApp;
    @MockitoBean private FirebaseMessaging firebaseMessaging;
    @MockitoBean private SimpMessagingTemplate messagingTemplate;

    @MockitoSpyBean private UserService userService;

    @Autowired private KickRoommateUseCase kickRoommateUseCase;
    @Autowired private RoomRepository roomRepository;
    @Autowired private RoommateRepository roommateRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatRoomMemberRepository chatRoomMemberRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;

    private static final String HOST_NO   = "test_kick_roommate_host_001";
    private static final String MEMBER_NO = "test_kick_roommate_member_001";

    private Room room;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        User fakeUser = User.builder()
                .nickname("MemberNick")
                .name("Member")
                .email("kick-member@test.com")
                .password("pw")
                .role(Role.USER)
                .studentNo("202400102")
                .build();
        doReturn(fakeUser).when(userService).findById(MEMBER_NO);

        room = roomRepository.save(Room.builder()
                .roomType(RoomType.TYPE_1)
                .capacity(2)
                .title("kick-room")
                .hostUserNo(HOST_NO)
                .residencePeriod(ResidencePeriod.SEMESTER)
                .gender(Gender.MALE)
                .build());
        room.plusCurrentMate();
        room = roomRepository.save(room);

        roommateRepository.save(Roommate.builder()
                .room(room)
                .userNo(HOST_NO)
                .roomRole(RoomRole.HOST)
                .confirmStatus(ConfirmStatus.ACCEPTED)
                .build());
        roommateRepository.save(Roommate.builder()
                .room(room)
                .userNo(MEMBER_NO)
                .roomRole(RoomRole.MEMBER)
                .confirmStatus(ConfirmStatus.ACCEPTED)
                .build());

        chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .roomNo(room.getRoomNo())
                .build());
        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(chatRoom).userNo(HOST_NO).build());
        ChatRoomMember memberChat = chatRoomMemberRepository.save(
                ChatRoomMember.builder().chatRoom(chatRoom).userNo(MEMBER_NO).build()
        );

        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderNo(HOST_NO)
                .content("hello")
                .messageType(MessageType.TEXT)
                .unreadCount(1)
                .build());
        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderNo("SYSTEM")
                .content("joined")
                .messageType(MessageType.SYSTEM)
                .unreadCount(0)
                .build());

        memberChat.updateLastReadAt(LocalDateTime.now().minusMinutes(1));
        chatRoomMemberRepository.save(memberChat);
    }

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAllInBatch();
        chatRoomMemberRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        roommateRepository.deleteAllInBatch();
        roomRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("강퇴 성공 시 룸메이트/채팅멤버/인원수가 실제 DB에 반영된다")
    void execute_PersistsKickChangesAcrossRoomAndChat() {
        kickRoommateUseCase.execute(HOST_NO, room.getRoomNo(), MEMBER_NO);

        Room persistedRoom = roomRepository.findById(room.getRoomNo()).orElseThrow();

        assertThat(roommateRepository.existsByUserNoAndRoomNo(MEMBER_NO, room.getRoomNo())).isFalse();
        assertThat(chatRoomMemberRepository.existsByChatRoomAndUserNo(chatRoom, MEMBER_NO)).isFalse();
        assertThat(persistedRoom.getCurrentMateCount()).isEqualTo(1);
        assertThat(persistedRoom.getRemaining()).isEqualTo(1);

        await().untilAsserted(() -> {
            verify(messagingTemplate).convertAndSend(
                    eq("/topic/chat-room/" + chatRoom.getChatRoomNo()), any(Object.class));
            verify(messagingTemplate).convertAndSendToUser(eq(MEMBER_NO), eq("/queue/notification"), any());
        });
    }
}
