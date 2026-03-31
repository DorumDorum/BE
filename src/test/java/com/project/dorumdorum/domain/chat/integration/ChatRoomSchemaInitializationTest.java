package com.project.dorumdorum.domain.chat.integration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ChatRoom schema 초기화 테스트")
class ChatRoomSchemaInitializationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockitoBean
    private FirebaseApp firebaseApp;

    @MockitoBean
    private FirebaseMessaging firebaseMessaging;

    @Test
    @DisplayName("테스트 DB에 DIRECT/GROUP partial unique index가 생성된다")
    void testIndexesAreCreatedForChatRoom() {
        List<String> indexDefinitions = jdbcTemplate.query(
                """
                SELECT indexdef
                FROM pg_indexes
                WHERE schemaname = 'public'
                  AND tablename = 'chat_room'
                  AND indexname IN ('uk_chat_room_group', 'uk_chat_room_direct')
                ORDER BY indexname
                """,
                (rs, rowNum) -> rs.getString("indexdef")
        );

        assertThat(indexDefinitions).hasSize(2);
        assertThat(indexDefinitions)
                .anySatisfy(indexDef -> {
                    assertThat(indexDef).contains("UNIQUE INDEX uk_chat_room_direct");
                    assertThat(indexDef).contains("DIRECT");
                })
                .anySatisfy(indexDef -> {
                    assertThat(indexDef).contains("UNIQUE INDEX uk_chat_room_group");
                    assertThat(indexDef).contains("GROUP");
                });
    }
}
