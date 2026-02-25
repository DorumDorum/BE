package com.project.dorumdorum.global.warmer;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class RequestActivityTrackingFilter extends OncePerRequestFilter {

    private final AtomicLong lastRequestTime = new AtomicLong(System.currentTimeMillis());

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        lastRequestTime.set(System.currentTimeMillis());
        filterChain.doFilter(request, response);
    }

    public long getLastRequestTime() {
        return lastRequestTime.get();
    }
}

