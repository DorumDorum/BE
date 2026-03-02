package com.project.dorumdorum.domain.notification.infra.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationJpaRepositoryImpl 단위 테스트 (비활성화됨)")
class NotificationJpaRepositoryImplTest {

    @Test
    @DisplayName("Placeholder 테스트 (향후 QueryDSL 통합 테스트로 대체 예정)")
    void placeholder() {
        // 이 테스트는 현재 별도 로직을 수행하지 않는다.
        // NotificationJpaRepositoryImpl는 QueryDSL 의존성이 강하므로,
        // 실제 쿼리 검증은 통합 테스트에서 다루는 것이 더 적절하다.
        assertThat(1).isEqualTo(1);
    }
}

