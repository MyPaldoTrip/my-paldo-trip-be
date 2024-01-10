package com.b6.mypaldotrip.global.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "Token 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtil.getTokenFromHeader(request);
        jwtUtil.validateToken(token);
        if (token != null) {
            Claims userInfo = jwtUtil.getUserInfoFromToken(token);
            try {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(userInfo.getSubject());
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            } catch (Exception e) {
                log.error("Authentication error: {}", e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
