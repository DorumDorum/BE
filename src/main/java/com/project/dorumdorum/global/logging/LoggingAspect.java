package com.project.dorumdorum.global.logging;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final DomainEventLogger domainEventLogger;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(logging)")
    public Object logDomainEvent(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        Object result = joinPoint.proceed();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvaluationContext context = new MethodBasedEvaluationContext(joinPoint.getTarget(), method, joinPoint.getArgs(), nameDiscoverer);
        context.setVariable("result", result);

        Map<String, Object> payload = new LinkedHashMap<>();
        for (String entry : logging.payload()) {
            int idx = entry.indexOf('=');
            if (idx <= 0 || idx >= entry.length() - 1) {
                continue;
            }
            String key = entry.substring(0, idx).trim();
            String expression = entry.substring(idx + 1).trim();
            Object value = evaluateSafely(context, expression);
            if (value != null) {
                payload.put(key, value);
            }
        }

        switch (logging.level()) {
            case WARN -> domainEventLogger.warn(logging.event(), logging.action(), payload, null);
            case ERROR -> domainEventLogger.error(logging.event(), logging.action(), payload, null);
            default -> domainEventLogger.info(logging.event(), logging.action(), payload);
        }

        return result;
    }

    private Object evaluateSafely(EvaluationContext context, String expression) {
        try {
            return parser.parseExpression(expression).getValue(context);
        } catch (Exception ignored) {
            return null;
        }
    }
}
