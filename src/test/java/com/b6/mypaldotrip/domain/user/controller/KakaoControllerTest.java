package com.b6.mypaldotrip.domain.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.user.CommonControllerTest;
import com.b6.mypaldotrip.domain.user.service.KakaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = {KakaoController.class})
@MockBean(JpaMetamodelMappingContext.class)
class KakaoControllerTest extends CommonControllerTest {

    @MockBean private KakaoService kakaoService;

    @Test
    @DisplayName("카카오 인증 코드 테스트")
    void 카카오인증코드() throws Exception {
        // given
        String expectedUrl = "testUrl";
        given(kakaoService.redirect()).willReturn(expectedUrl);
        // when
        ResultActions actions =
                mockMvc.perform(get("/api/" + versionConfig.getVersion() + "/users/kakao-code"));
        // then
        actions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedUrl))
                .andDo(
                        document(
                                "user/kakao-code",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("카카오 로그인 테스트")
    void 카카오로그인() throws Exception {
        // given
        String code = "testCode";
        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/api/" + versionConfig.getVersion() + "/users/kakao-login")
                                .param("code", code));
        // then
        actions.andExpect(status().isOk())
                .andDo(
                        document(
                                "user/kakao-login",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }
}
