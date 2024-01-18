package com.b6.mypaldotrip.domain.user.controller;

import com.b6.mypaldotrip.domain.user.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao-login")
    public String homepage() {
        return "kakao";
    }

    @GetMapping("${kakao.redirect-uri}")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response)
            throws JsonProcessingException {
        kakaoService.kakaoLogin(code, response);
        return "redirect:http://localhost:8000";
    }
}
