package com.project.dorumdorum.domain.room.integration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.dorumdorum.domain.room.application.usecase.DecideApplicationRequestUseCase;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.repository.RoomRepository;
import com.project.dorumdorum.domain.room.domain.repository.RoomRequestRepository;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import com.project.dorumdorum.domain.roommate.domain.repository.RoommateRepository;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.ROOM_FULL;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("DecideApplicationRequest 동시성 재현 테스트")
class DecideApplicationRequestConcurrencyTest {

    @Autowired
    private DecideApplicationRequestUseCase decideApplicationRequestUseCase;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomRequestRepository roomRequestRepository;

    @Autowired
    private RoommateRepository roommateRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private ApplicationEventPublisher applicationEventPublisher;

    @MockitoBean
    private FirebaseApp firebaseApp;

    @MockitoBean
    private FirebaseMessaging firebaseMessaging;

    @AfterEach
    void tearDown() {
        roomRequestRepository.deleteAllInBatch();
        roommateRepository.deleteAllInBatch();
        roomRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("여러 지원자를 동시에 승인해도 정원은 초과되지 않고 한 명만 승인된다")
    void approve_Concurrently_DoesNotExceedCapacity() throws Exception {

        // 테스트 환경 구성
        int concurrentRequests = 10;
        User host = saveUser("host1@example.com");
        String hostUserNo = host.getUserNo();
        Room room = roomRepository.save(Room.builder()
                .roomType(RoomType.TYPE_1)
                .capacity(2)
                .residencePeriod(ResidencePeriod.SEMESTER)
                .title("room")
                .hostUserNo(hostUserNo)
                .build());

        roommateRepository.save(Roommate.builder()
                .room(room)
                .userNo(hostUserNo)
                .roomRole(RoomRole.HOST)
                .build());

        List<RoomRequest> requests = new ArrayList<>();
        for (int i = 0; i < concurrentRequests; i++) {
            User applicant = saveUser("user" + i + "@example.com");
            requests.add(roomRequestRepository.save(RoomRequest.builder()
                    .room(room)
                    .userNo(applicant.getUserNo())
                    .direction(Direction.USER_TO_ROOM)
                    .introduction("intro-" + i)
                    .additionalMessage("msg-" + i)
                    .build()));
        }

        // 테스트 시작
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentRequests);
        CountDownLatch readyLatch = new CountDownLatch(concurrentRequests);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(concurrentRequests);
        List<Future<Throwable>> futures = new ArrayList<>();
        try {
            for (RoomRequest request : requests) {
                futures.add(executorService.submit(() -> {
                    readyLatch.countDown();
                    try {
                        startLatch.await();
                        decideApplicationRequestUseCase.approve(hostUserNo, room.getRoomNo(), request.getRoomRequestNo());
                        return null;
                    } catch (Throwable t) {
                        return t;
                    } finally {
                        doneLatch.countDown();
                    }
                }));
            }

            assertThat(readyLatch.await(5, TimeUnit.SECONDS)).isTrue();
            startLatch.countDown();
            assertThat(doneLatch.await(10, TimeUnit.SECONDS)).isTrue();
        } finally {
            executorService.shutdown();
        }

        List<Throwable> errors = new ArrayList<>();
        long successCount = 0;
        for (Future<Throwable> future : futures) {
            Throwable error = future.get(5, TimeUnit.SECONDS);
            if (error == null) {
                successCount++;
                continue;
            }
            errors.add(error);
        }

        Room reloadedRoom = roomRepository.findById(room.getRoomNo()).orElseThrow();
        long roommateCount = roommateRepository.findByRoom(reloadedRoom).size();
        long remainingRequests = roomRequestRepository.count();

        long failureCount = errors.stream()
                .filter(RestApiException.class::isInstance)
                .map(RestApiException.class::cast)
                .filter(exception -> ROOM_FULL.getCode().getCode().equals(exception.getErrorCode().getCode()))
                .count();

        assertThat(reloadedRoom.getCapacity()).isEqualTo(2);
        assertThat(reloadedRoom.getCurrentMateCount()).isEqualTo(2);
        assertThat(roommateCount).isEqualTo(2);
        assertThat(roommateCount).isEqualTo(reloadedRoom.getCurrentMateCount().longValue());
        assertThat(successCount).isEqualTo(1);
        assertThat(failureCount).isEqualTo(concurrentRequests - 1L);
        assertThat(remainingRequests).isEqualTo(concurrentRequests - 1L);
    }

    private User saveUser(String email) {
        return userRepository.save(User.builder()
                .name(email)
                .nickname(email)
                .email(email)
                .password("password")
                .role(Role.USER)
                .studentNo(email)
                .major("major")
                .grade("1")
                .birth("2000-01-01")
                .age(26)
                .build());
    }
}
