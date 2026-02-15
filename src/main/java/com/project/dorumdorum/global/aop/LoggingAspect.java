package com.project.dorumdorum.global.aop;

import com.project.dorumdorum.global.logging.LogRedactor;
import com.project.dorumdorum.global.logging.RequestLogContext;
import com.project.dorumdorum.global.logging.RequestLogContextResolver;
import com.project.dorumdorum.global.logging.StructuredLogFactory;
import com.project.dorumdorum.global.properties.LoggingPolicyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.entries;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final RequestLogContextResolver requestLogContextResolver;
    private final LogRedactor logRedactor;
    private final StructuredLogFactory structuredLogFactory;
    private final LoggingPolicyProperties loggingPolicyProperties;

    @Around("execution(* com.project.dorumdorum..*Controller.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = getCurrentRequest();
        HttpServletResponse response = getCurrentResponse();
        RequestLogContext requestContext = requestLogContextResolver.resolve(request, response, 200);
        String argsMessage = logRedactor.redactArgs(args);

        Map<String, Object> requestLog = structuredLogFactory.requestReceived(requestContext, argsMessage);
        log.info("요청 수신 {}", entries(requestLog));

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            String redactedResult = logRedactor.redactResult(result);
            int responseSize = redactedResult.getBytes(StandardCharsets.UTF_8).length;
            RequestLogContext successContext = requestLogContextResolver.resolve(request, response, 200);
            Map<String, Object> responseLog = structuredLogFactory.requestCompleted(successContext, elapsed, responseSize);
            responseLog = append(responseLog, "result", redactedResult);
            log.info("요청 완료 {}", entries(responseLog));

            return result;
        } catch (Throwable e) {
            long elapsed = System.currentTimeMillis() - start;
            RequestLogContext failureContext = requestLogContextResolver.resolve(request, response, 500);
            String redactedMessage = logRedactor.redactText(e.getMessage());
            Map<String, Object> errorLog = structuredLogFactory.requestFailed(failureContext, e, elapsed, redactedMessage);
            if (loggingPolicyProperties.includeStackTrace()) {
                log.error("요청 실패 {}", entries(errorLog), e);
            } else {
                log.error("요청 실패 {}", entries(errorLog));
            }
            throw e;
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

    private Map<String, Object> append(Map<String, Object> source, String key, Object value) {
        java.util.LinkedHashMap<String, Object> map = new java.util.LinkedHashMap<>(source);
        map.put(key, value);
        return Map.copyOf(map);
    }
}
