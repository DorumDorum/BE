package com.project.dorumdorum.global.exception;

import com.project.dorumdorum.global.alert.SystemAlertPublisher;
import com.project.dorumdorum.global.exception.code.status.CommonErrorStatus;
import com.project.dorumdorum.global.logging.LogRedactor;
import com.project.dorumdorum.global.logging.RequestLogContext;
import com.project.dorumdorum.global.logging.RequestLogContextResolver;
import com.project.dorumdorum.global.logging.StructuredLogFactory;
import com.project.dorumdorum.global.properties.LoggingPolicyProperties;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExceptionAdvice Unit Tests")
class ExceptionAdviceTest {

    @Mock
    private RequestLogContextResolver requestLogContextResolver;

    @Mock
    private StructuredLogFactory structuredLogFactory;

    @Mock
    private LogRedactor logRedactor;

    @Mock
    private LoggingPolicyProperties loggingPolicyProperties;

    @Mock
    private SystemAlertPublisher systemAlertPublisher;

    private ExceptionAdvice exceptionAdvice;

    @BeforeEach
    void setUp() {
        exceptionAdvice = new ExceptionAdvice(
                requestLogContextResolver,
                structuredLogFactory,
                logRedactor,
                loggingPolicyProperties,
                systemAlertPublisher
        );

        lenient().when(requestLogContextResolver.resolve(any(), any(), anyInt()))
                .thenReturn(new RequestLogContext("trace-1", "GET", "/api/test", null, null, 500));
        lenient().when(logRedactor.redactText(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(structuredLogFactory.requestFailed(any(), any(), anyLong(), anyString()))
                .thenReturn(Map.of("event", "request_failed"));
        lenient().when(loggingPolicyProperties.includeStackTrace()).thenReturn(false);
    }

    @Test
    @DisplayName("room request unique 제약 위반은 DUPLICATE_JOIN_REQUEST로 변환한다")
    void handleDataIntegrityViolationException_WhenDuplicateRoomRequest_ReturnsBadRequest() {
        SQLException sqlException = new SQLException("duplicate key", "23505");
        ConstraintViolationException cause = new ConstraintViolationException(
                "duplicate",
                sqlException,
                "uk_room_request_user_room_direction"
        );
        DataIntegrityViolationException exception = new DataIntegrityViolationException("duplicate", cause);

        ResponseEntity<ErrorResponse> response = exceptionAdvice.handleDataIntegrityViolationException(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("ROOM002");
        verify(systemAlertPublisher, never()).publish(any(), any(), any(), any());
    }

    @Test
    @DisplayName("roommate unique 제약 위반은 ALREADY_JOINED_USER로 변환한다")
    void handleDataIntegrityViolationException_WhenDuplicateRoommate_ReturnsBadRequest() {
        SQLException sqlException = new SQLException("duplicate key", "23505");
        ConstraintViolationException cause = new ConstraintViolationException(
                "duplicate",
                sqlException,
                "uk_roommate_user_no"
        );
        DataIntegrityViolationException exception = new DataIntegrityViolationException("duplicate", cause);

        ResponseEntity<ErrorResponse> response = exceptionAdvice.handleDataIntegrityViolationException(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("ROOM013");
        verify(systemAlertPublisher, never()).publish(any(), any(), any(), any());
    }

    @Test
    @DisplayName("user unique 제약 위반은 DUPLICATE_SIGN_UP_INFO로 변환한다")
    void handleDataIntegrityViolationException_WhenDuplicateUserSignUp_ReturnsBadRequest() {
        SQLException sqlException = new SQLException("duplicate key", "23505");
        ConstraintViolationException cause = new ConstraintViolationException(
                "duplicate",
                sqlException,
                "uk_user_email"
        );
        DataIntegrityViolationException exception = new DataIntegrityViolationException("duplicate", cause);

        ResponseEntity<ErrorResponse> response = exceptionAdvice.handleDataIntegrityViolationException(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("USER001");
        verify(systemAlertPublisher, never()).publish(any(), any(), any(), any());
    }

    @Test
    @DisplayName("chat room member unique 제약 위반은 ALREADY_CHAT_ROOM_MEMBER로 변환한다")
    void handleDataIntegrityViolationException_WhenDuplicateChatRoomMember_ReturnsBadRequest() {
        SQLException sqlException = new SQLException("duplicate key", "23505");
        ConstraintViolationException cause = new ConstraintViolationException(
                "duplicate",
                sqlException,
                "uk_chat_room_member_room_user"
        );
        DataIntegrityViolationException exception = new DataIntegrityViolationException("duplicate", cause);

        ResponseEntity<ErrorResponse> response = exceptionAdvice.handleDataIntegrityViolationException(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("CHAT004");
        verify(systemAlertPublisher, never()).publish(any(), any(), any(), any());
    }

    @Test
    @DisplayName("알 수 없는 DataIntegrityViolationException은 500으로 처리한다")
    void handleDataIntegrityViolationException_WhenUnknown_ReturnsInternalServerError() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("unexpected");

        ResponseEntity<ErrorResponse> response = exceptionAdvice.handleDataIntegrityViolationException(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode())
                .isEqualTo(CommonErrorStatus._INTERNAL_SERVER_ERROR.getCode().getCode());
        verify(systemAlertPublisher).publish(any(), any(), any(), any());
    }
}
