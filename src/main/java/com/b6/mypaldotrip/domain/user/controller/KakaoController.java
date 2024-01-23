package com.b6.mypaldotrip.domain.user.controller;

import com.b6.mypaldotrip.domain.user.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @ResponseBody
    @GetMapping("/api/${mpt.version}/users/kakao-code")
    public RedirectView redirectView() {
        String url = kakaoService.redirect();
        return new RedirectView(url);
    }

    @GetMapping("/api/${mpt.version}/users/kakao-login")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response)
            throws JsonProcessingException {
        kakaoService.kakaoLogin(code, response);
    }
}
