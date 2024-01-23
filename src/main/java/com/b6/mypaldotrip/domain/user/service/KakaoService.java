package com.b6.mypaldotrip.domain.user.service;

import static com.b6.mypaldotrip.global.security.JwtUtil.TOKEN_HEADER;

import com.b6.mypaldotrip.domain.user.controller.dto.request.UserSignUpReq;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import com.b6.mypaldotrip.global.security.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${kakao.rest-api-key}")
    private String CLIENT_ID;

    @Value("${kakao.secret-key}")
    private String SECRET_KEY;

    @Value("${kakao.target-ip}")
    private String TARGET_IP;

    public void kakaoLogin(String code, HttpServletResponse response)
            throws JsonProcessingException {
        String accessToken = getToken(code);
        UserSignUpReq req = getKaKaoUserInfo(accessToken);
        UserEntity userEntity =
                UserEntity.builder()
                        .email(req.email())
                        .username(req.username())
                        .password(req.password())
                        .build();
        if (userRepository.findByEmail(req.email()).isEmpty()) {
            userRepository.save(userEntity);
        } else {
            String token = jwtUtil.createToken(userEntity.getEmail());
            log.info(token);
            response.addHeader(TOKEN_HEADER, token);
        }
    }

    // 토큰 받기 api
    private String getToken(String code) throws JsonProcessingException {

        String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
        String GRANT_TYPE = "?grant_type=authorization_code";

        RestClient restClient = RestClient.create();
        String response =
                restClient
                        .post()
                        .uri(
                                TOKEN_URI
                                        + GRANT_TYPE
                                        + "&client_id="
                                        + CLIENT_ID
                                        + "&redirect_uri="
                                        + TARGET_IP
                                        + REDIRECT_URI
                                        + "&code="
                                        + code
                                        + "&client_secret="
                                        + SECRET_KEY)
                        .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                        .retrieve()
                        .body(String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response);

        return jsonNode.get("access_token").asText();
    }
    // 사용자 정보 가져오기 api
    private UserSignUpReq getKaKaoUserInfo(String accessToken) throws JsonProcessingException {

        String INFO_URI = "https://kapi.kakao.com/v2/user/me";

        RestClient restClient = RestClient.create();
        String response =
                restClient
                        .get()
                        .uri(INFO_URI)
                        .header("Authorization", "Bearer " + accessToken)
                        .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                        .retrieve()
                        .body(String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response);
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return UserSignUpReq.builder()
                .email(email)
                .password(UUID.randomUUID().toString())
                .username(nickname)
                .build();
    }

    public String redirect() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + CLIENT_ID
                + "&redirect_uri="
                + TARGET_IP
                + REDIRECT_URI;
    }
}
