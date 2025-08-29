package com.project.dorumdorum.global.config;

import com.project.dorumdorum.global.interceptor.JwtBlacklistInterceptor;
import com.project.dorumdorum.global.resolver.AccessTokenArgumentResolver;
import com.project.dorumdorum.global.resolver.CurrentUserArgumentResolver;
import com.project.dorumdorum.global.resolver.RefreshTokenArgumentResolver;
import com.project.dorumdorum.global.properties.ExcludeBlacklistPathProperties;
import com.project.dorumdorum.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;
    private final JwtBlacklistInterceptor jwtBlacklistInterceptor;
    private final ExcludeBlacklistPathProperties excludeBlacklistPathProperties;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(List.of(
                new CurrentUserArgumentResolver(tokenProvider),
                new RefreshTokenArgumentResolver(tokenProvider),
                new AccessTokenArgumentResolver(tokenProvider)
        ));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtBlacklistInterceptor)
                .excludePathPatterns(excludeBlacklistPathProperties.getExcludeAuthPaths());
        }

    @Value("${app.storage.local.base-dir:./uploads}")
    private String baseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://localhost:8080/files/** -> file:{baseDir}/**
        registry.addResourceHandler("/files/**") // files로 오는 요청을 모두 처리
                .addResourceLocations("file:" + baseDir + "/"); // 그런 요청을 해당 위치에서 찾아줄 것
    }
}
