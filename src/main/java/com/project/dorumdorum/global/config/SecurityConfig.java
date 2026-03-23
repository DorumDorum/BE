package com.project.dorumdorum.global.config;

import com.project.dorumdorum.domain.user.domain.service.TokenWhitelistService;
import com.project.dorumdorum.global.alert.SystemAlertPublisher;
import com.project.dorumdorum.global.logging.RequestLoggingFilter;
import com.project.dorumdorum.global.logging.RequestLogContextResolver;
import com.project.dorumdorum.global.logging.StructuredLogFactory;
import com.project.dorumdorum.global.properties.ExcludeAuthPathProperties;
import com.project.dorumdorum.global.properties.ExcludeWhitelistPathProperties;
import com.project.dorumdorum.global.properties.LoggingPolicyProperties;
import com.project.dorumdorum.global.warmer.RequestActivityTrackingFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dorumdorum.global.security.JwtAuthenticationFilter;
import com.project.dorumdorum.global.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final ExcludeAuthPathProperties excludeAuthPathProperties;
    private final TokenWhitelistService tokenWhitelistService;
    private final ExcludeWhitelistPathProperties excludeWhitelistPathProperties;
    private final RequestLogContextResolver requestLogContextResolver;
    private final StructuredLogFactory structuredLogFactory;
    private final LoggingPolicyProperties loggingPolicyProperties;
    private final SystemAlertPublisher systemAlertPublisher;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(request -> {
            excludeAuthPathProperties.getPaths().iterator()
                    .forEachRemaining(authPath ->
                            request.requestMatchers(HttpMethod.valueOf(authPath.getMethod()), authPath.getPathPattern()).permitAll()
                    );
            request.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
            request.requestMatchers("/ws/**", "/ws", "/ws-native/**", "/ws-native").permitAll();
            request.requestMatchers(HttpMethod.GET, "/api/notifications/stream").permitAll();
            request.requestMatchers("/error").permitAll();
        });

        http.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.POST, "/api/token/reissue").permitAll()
                .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(
                requestActivityTrackingFilter(),
                UsernamePasswordAuthenticationFilter.class
        );
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(
                new RequestLoggingFilter(requestLogContextResolver, structuredLogFactory, loggingPolicyProperties, systemAlertPublisher),
                JwtAuthenticationFilter.class
        );

        http.exceptionHandling(except -> except
                .authenticationEntryPoint((request, response, authException) -> response.sendError(response.getStatus(), "토큰 오류"))
        );

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                tokenProvider,
                excludeAuthPathProperties,
                tokenWhitelistService,
                excludeWhitelistPathProperties,
                objectMapper
        );
    }

    @Bean
    public RequestActivityTrackingFilter requestActivityTrackingFilter() {
        return new RequestActivityTrackingFilter();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:5173",
            "https://dorumdorum.com",
            "https://www.dorumdorum.com"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
