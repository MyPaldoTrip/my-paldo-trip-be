package com.b6.mypaldotrip.domain.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.user.ApplicationCommonTest;
import com.b6.mypaldotrip.domain.user.CommonControllerTest;
import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationSubmitReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationSubmitRes;
import com.b6.mypaldotrip.domain.user.service.ApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = {ApplicationController.class})
@MockBean(JpaMetamodelMappingContext.class)
class ApplicationControllerTest extends CommonControllerTest implements ApplicationCommonTest {

    @MockBean private ApplicationService applicationService;

    @Nested
    @DisplayName("신청서 제출 테스트")
    class 신청서제출{
        @Test
        @DisplayName("신청서 제출 테스트 성공")
        void 신청서제출1 () throws Exception {
            //given
            ApplicationSubmitReq req = ApplicationSubmitReq.builder().title(TEST_TITLE).content(TEST_CONTENT).build();
            ApplicationSubmitRes res = ApplicationSubmitRes.builder().email(TEST_EMAIL).username(TEST_USERNAME)
                .title(TEST_TITLE).content(TEST_CONTENT).build();
            given(applicationService.submit(req, TEST_USER)).willReturn(res);
            //when
            ResultActions actions = mockMvc.perform(
                post("/api/" + versionConfig.getVersion() + "/users/application")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)));
            //then
            actions.andExpect(status().isCreated())
                .andDo(
                    document(
                        "user/application-submit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        }
        @Test
        @DisplayName("신청서 제출 테스트 실패")
        void 신청서제출2 () throws Exception {
            //given
            ApplicationSubmitReq req = ApplicationSubmitReq.builder().build();
            //when
            ResultActions actions = mockMvc.perform(
                post("/api/" + versionConfig.getVersion() + "/users/application")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)));
            //then
            actions.andExpect(status().isBadRequest());
        }
    }
}