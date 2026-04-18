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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

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

    @Autowired private KickRoommateUseCase kickRoommateUseCase;
    @Autowired private UserRepository userRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private RoommateRepository roommateRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatRoomMemberRepository chatRoomMemberRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;

    private static final String HOST_NO = "kick-host-001";
    private static final String MEMBER_NO = "kick-member-001";

    private Room room;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .userNo(HOST_NO)
                .email("kick-host@gachon.ac.kr")
                .password("pw")
                .role(Role.USER)
                .studentNo("202400101")
                .name("Host")
                .nickname("HostNick")
                .gender(Gender.MALE)
                .build());
        userRepository.save(User.builder()
                .userNo(MEMBER_NO)
                .email("kick-member@gachon.ac.kr")
                .password("pw")
                .role(Role.USER)
                .studentNo("202400102")
                .name("Member")
                .nickname("MemberNick")
                .gender(Gender.MALE)
                .build());

        room = roomRepository.save(Room.builder()
                .roomType(RoomType.TYPE_1)
                .capacity(2)
                .title("kick-room")
                .hostUserNo(HOST_NO)
                .residencePeriod(ResidencePeriod.SEMESTER)
                .gender(Gender.MALE)
                .build());
        room.plusCurrentMate();

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
        chatMessageRepository.deleteAll();
        chatRoomMemberRepository.deleteAll();
        chatRoomRepository.deleteAll();
        roommateRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
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

        verify(messagingTemplate).convertAndSendToUser(eq(MEMBER_NO), eq("/queue/notification"), any());
    }
}
