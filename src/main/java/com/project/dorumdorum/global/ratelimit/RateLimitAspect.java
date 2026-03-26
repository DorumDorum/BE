package com.project.dorumdorum.global.ratelimit;

import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.project.dorumdorum.global.exception.code.status.CommonErrorStatus._TOO_MANY_REQUEST;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class RateLimitAspect {

    private final SlidingWindowRateLimiter slidingWindowRateLimiter;
    private final RateLimitPolicyRegistry rateLimitPolicyRegistry;

    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(rateLimited)")
    public Object applyRateLimit(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        String subjectKey = evaluateKey(joinPoint, rateLimited.key());
        RateLimitRule rule = rateLimitPolicyRegistry.get(rateLimited.tag());

        if (slidingWindowRateLimiter.isRateLimited(rateLimited.tag(), subjectKey, rule)) {
            throw new RestApiException(_TOO_MANY_REQUEST);
        }

        return joinPoint.proceed();
    }

    private String evaluateKey(ProceedingJoinPoint joinPoint, String expression) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Object[] arguments = joinPoint.getArgs();

        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < arguments.length; i++) {
            context.setVariable("p" + i, arguments[i]);
            context.setVariable("a" + i, arguments[i]);
        }
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], arguments[i]);
            }
        }

        return expressionParser.parseExpression(expression).getValue(context, String.class);
    }
}
