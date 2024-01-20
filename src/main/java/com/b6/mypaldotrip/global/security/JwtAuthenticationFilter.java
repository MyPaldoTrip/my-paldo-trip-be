package com.b6.mypaldotrip.global.security;

import static com.b6.mypaldotrip.global.security.JwtUtil.TOKEN_HEADER;

import com.b6.mypaldotrip.domain.user.controller.dto.request.UserLonginReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserLoginRes;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 Token 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, VersionConfig versionConfig) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/" + versionConfig.getVersion() + "/users/login");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) {
        try {
            UserLonginReq req =
                    new ObjectMapper().readValue(request.getInputStream(), UserLonginReq.class);
            return getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            Authentication authentication) {

        UserEntity userEntity = ((UserDetailsImpl) authentication.getPrincipal()).getUserEntity();

        String token = jwtUtil.createToken(userEntity.getEmail());

        response.addHeader(TOKEN_HEADER, token);

        sendResponse(response, HttpStatus.OK.value(), "login success.");
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authentication) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        sendResponse(response, HttpStatus.UNAUTHORIZED.value(), "login failed.");
    }

    private void sendResponse(HttpServletResponse response, Integer code, String message) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        UserLoginRes res = UserLoginRes.builder().code(code).message(message).build();
        try {
            response.getWriter().println(new ObjectMapper().writeValueAsString(res));
        } catch (IOException e) {
            log.error("Response writing failed: {}", e.getMessage());
        }
    }
}
