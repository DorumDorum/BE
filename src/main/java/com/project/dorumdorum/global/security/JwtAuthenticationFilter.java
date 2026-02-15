package com.project.dorumdorum.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dorumdorum.domain.user.domain.service.TokenWhitelistService;
import com.project.dorumdorum.global.exception.ErrorResponse;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.BaseCode;
import com.project.dorumdorum.global.exception.code.status.CommonErrorStatus;
import com.project.dorumdorum.global.properties.ExcludeAuthPathProperties;
import com.project.dorumdorum.global.properties.ExcludeWhitelistPathProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.time.Duration;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.EMPTY_JWT;
import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.INVALID_ACCESS_TOKEN;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ExcludeAuthPathProperties excludeAuthPathProperties;
    private final TokenWhitelistService tokenWhitelistService;
    private final ExcludeWhitelistPathProperties excludeWhitelistPathProperties;
    private static final PathPatternParser pathPatternParser = new PathPatternParser();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isExcludedPath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = tokenProvider.getToken(request)
                    .orElseThrow(() -> new RestApiException(EMPTY_JWT));

            if (tokenWhitelistService.isWhitelistToken(token)) {
                setAuthentication(token);
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰 검증
            if(tokenProvider.validateToken(token))
                setAuthentication(token);
            else
                throw new RestApiException(INVALID_ACCESS_TOKEN);

            if(isExcludedPathForWhitelist(request)) {
                tokenWhitelistService.whitelist(token, Duration.ofSeconds(30));
            }

            filterChain.doFilter(request, response);
        } catch (RestApiException e) {
            writeErrorResponse(response, e.getErrorCode());
        } catch (Exception e) {
            writeErrorResponse(response, CommonErrorStatus._INTERNAL_SERVER_ERROR.getCode());
        }
    }

    public boolean isExcludedPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());

        return excludeAuthPathProperties.getPaths().stream()
                .anyMatch(authPath ->
                        pathPatternParser.parse(authPath.getPathPattern())
                                .matches(PathContainer.parsePath(requestPath))
                        && requestMethod.equals(HttpMethod.valueOf(authPath.getMethod()))
                );
    }

    public boolean isExcludedPathForWhitelist(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());

        return excludeWhitelistPathProperties.getPaths().stream()
                .anyMatch(authPath ->
                        pathPatternParser.parse(authPath.getPathPattern())
                                .matches(PathContainer.parsePath(requestPath))
                                && requestMethod.equals(HttpMethod.valueOf(authPath.getMethod()))
                );
    }

    private void setAuthentication(String token) {
        if (tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, BaseCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse body = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(body));
        response.getWriter().flush();
    }
}
