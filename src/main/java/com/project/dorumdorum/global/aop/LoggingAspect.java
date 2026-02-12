package com.project.dorumdorum.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private static final List<String> SENSITIVE_PATTERNS = List.of(
            "password",
            "accesstoken",
            "refreshtoken",
            "verificationcode",
            "apikey",
            "apisecret",
            "email",
            "phone",
            "phonenumber",
            "fcmtoken",
            "address",
            "username",
            "token"
    );

    @Around("execution(* com.project.dorumdorum..*Controller.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("[Request] {}.{}() | args={}",
                className,
                methodName,
                formatArgs(args));

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;

            log.info("[Response] {}.{}() | elapsed={}ms | result={}",
                    className,
                    methodName,
                    elapsed,
                    formatResult(result));

            return result;
        } catch (Throwable e) {
            long elapsed = System.currentTimeMillis() - start;

            log.error("[Exception] {}.{}() | elapsed={}ms | exception={}: {}",
                    className,
                    methodName,
                    elapsed,
                    e.getClass().getSimpleName(),
                    e.getMessage());

            throw e;
        }
    }

    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        return Arrays.stream(args)
                .map(this::formatArg)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private String formatArg(Object arg) {
        if (arg == null) {
            return "null";
        }
        String str = arg.toString();
        if (containsSensitiveData(str)) {
            return "[MASKED]";
        }
        return str.length() > 200 ? str.substring(0, 200) + "..." : str;
    }

    private String formatResult(Object result) {
        if (result == null) {
            return "void";
        }
        String str = result.toString();
        if (containsSensitiveData(str)) {
            return "[MASKED]";
        }
        return str.length() > 300 ? str.substring(0, 300) + "..." : str;
    }

    private boolean containsSensitiveData(String str) {
        String lower = str.toLowerCase();
        return SENSITIVE_PATTERNS.stream()
                .anyMatch(lower::contains);
    }
}
