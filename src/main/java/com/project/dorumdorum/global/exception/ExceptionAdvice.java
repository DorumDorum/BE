package com.project.dorumdorum.global.exception;

import com.project.dorumdorum.global.alert.AlertSeverity;
import com.project.dorumdorum.global.alert.AlertType;
import com.project.dorumdorum.global.alert.SystemAlertPublisher;
import com.project.dorumdorum.global.exception.code.BaseCode;
import com.project.dorumdorum.global.exception.code.status.CommonErrorStatus;
import com.project.dorumdorum.global.logging.LogRedactor;
import com.project.dorumdorum.global.logging.RequestLogContext;
import com.project.dorumdorum.global.logging.RequestLogContextResolver;
import com.project.dorumdorum.global.logging.StructuredLogFactory;
import com.project.dorumdorum.global.properties.LoggingPolicyProperties;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.entries;

@Slf4j
@Hidden
@RestControllerAdvice(annotations = {RestController.class})
@RequiredArgsConstructor
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private final RequestLogContextResolver requestLogContextResolver;
    private final StructuredLogFactory structuredLogFactory;
    private final LogRedactor logRedactor;
    private final LoggingPolicyProperties loggingPolicyProperties;
    private final SystemAlertPublisher systemAlertPublisher;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle500Exception(Exception e) {
        logError(e, 500);

        HttpServletRequest request = getCurrentRequest();
        String path = request != null ? request.getRequestURI() : "unknown";
        String method = request != null ? request.getMethod() : "unknown";

        systemAlertPublisher.publish(
                AlertSeverity.CRITICAL,
                AlertType.SYSTEM_HEALTH,
                "[CRITICAL] 예상치 못한 500 에러 발생",
                method + " " + path + " - " + e.getClass().getSimpleName() + ": " + e.getMessage()
        );

        return handleExceptionInternal(CommonErrorStatus._INTERNAL_SERVER_ERROR.getCode());
    }

    /*
     * 직접 정의한 RestApiException 에러 클래스에 대한 예외 처리
     */
    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<ErrorResponse> handleRestApiException(RestApiException e) {
        BaseCode errorCode = e.getErrorCode();
        int status = errorCode.getHttpStatus().value();

        logError(e, status);

        if (errorCode.getHttpStatus().is5xxServerError()) {
            HttpServletRequest request = getCurrentRequest();
            String path = request != null ? request.getRequestURI() : "unknown";
            String method = request != null ? request.getMethod() : "unknown";

            systemAlertPublisher.publish(
                    AlertSeverity.ERROR,
                    AlertType.SYSTEM_HEALTH,
                    "[ERROR] 서버 에러 코드 발생 - " + errorCode.getCode(),
                    method + " " + path + " - " + errorCode.getMessage()
            );
        }

        return handleExceptionInternal(errorCode);
    }

    /*
     * ConstraintViolationException 발생 시 예외 처리
     * 메서드 파라미터, 또는 메서드 리턴 값에 문제가 있을 경우, @Validated 검증 실패한 경우
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        logError(e, CommonErrorStatus._VALIDATION_ERROR.getCode().getHttpStatus().value());
        return handleExceptionInternal(CommonErrorStatus._VALIDATION_ERROR.getCode());
    }

    /*
     * MethodArgumentTypeMismatchException 발생 시 예외 처리
     * 메서드의 인자 타입이 예상과 다른 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        logError(e, CommonErrorStatus._METHOD_ARGUMENT_ERROR.getCode().getHttpStatus().value());
        return handleExceptionInternal(CommonErrorStatus._METHOD_ARGUMENT_ERROR.getCode());
    }

    /*
     *  MethodArgumentNotValidException 발생 시 예외 처리
     * @@RequestBody 내부에서 처리 실패한 경우, @Valid 검증 실패한 경우 (ArgumnetResolver에 의해 유효성 검사)
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        logError(e, CommonErrorStatus._VALIDATION_ERROR.getCode().getHttpStatus().value());
        return ResponseEntity
                .status(CommonErrorStatus._VALIDATION_ERROR.getCode().getHttpStatus().value())
                .body(ErrorResponse.of(
                        CommonErrorStatus._VALIDATION_ERROR.getCode().getCode(),
                        CommonErrorStatus._VALIDATION_ERROR.getCode().getMessage(),
                        errors));

    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(BaseCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
    }

    private void logError(Exception e, int fallbackStatus) {
        HttpServletRequest request = getCurrentRequest();
        HttpServletResponse response = getCurrentResponse();
        RequestLogContext context = requestLogContextResolver.resolve(request, response, fallbackStatus);
        String message = logRedactor.redactText(e.getMessage() == null ? "" : e.getMessage());
        Map<String, Object> payload = structuredLogFactory.requestFailed(context, e, 0L, message);
        boolean isServerError = fallbackStatus >= 500;
        if (isServerError) {
            if (loggingPolicyProperties.includeStackTrace()) {
                log.error("요청 실패 {}", entries(payload), e);
            } else {
                log.error("요청 실패 {}", entries(payload));
            }
        } else {
            log.info("요청 실패 {}", entries(payload));
        }
    }

    private HttpServletRequest getCurrentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    private HttpServletResponse getCurrentResponse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getResponse();
        }
        return null;
    }
}
