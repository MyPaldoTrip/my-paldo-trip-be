package com.b6.mypaldotrip.domain.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.user.ApplicationCommonTest;
import com.b6.mypaldotrip.domain.user.CommonControllerTest;
import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationCheckReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationSubmitReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationConfirmRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationSubmitRes;
import com.b6.mypaldotrip.domain.user.service.ApplicationService;
import java.util.List;
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

    @Test
    @DisplayName("신청서 목록 조회 테스트 성공")
    void 신청서목록조회 () throws Exception {
        //given
        ApplicationGetListRes res = ApplicationGetListRes.builder().applicationId(TEST_APPLICATION_ID)
            .email(TEST_EMAIL).username(TEST_USERNAME).title(TEST_TITLE).verified(DEFAULT_VERIFIED).build();
        given(applicationService.getList()).willReturn(List.of(res));
        //when
        ResultActions actions = mockMvc.perform(
            get("/api/" + versionConfig.getVersion() + "/users/application"));
        //then
        actions.andExpect(status().isOk())
            .andDo(
                document(
                    "user/application-getList",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("신청서 단건 조회 테스트 성공")
    void 신청서단건조회 () throws Exception {
        //given
        ApplicationGetRes res = ApplicationGetRes.builder().applicationId(TEST_APPLICATION_ID)
            .email(TEST_EMAIL).username(TEST_USERNAME).title(TEST_TITLE).content(TEST_CONTENT).verified(DEFAULT_VERIFIED).build();
        given(applicationService.getApplication(TEST_APPLICATION_ID)).willReturn(res);
        //when
        ResultActions actions = mockMvc.perform(
            get("/api/" + versionConfig.getVersion() + "/users/application/{applicationId}",TEST_APPLICATION_ID));
        //then
        actions.andExpect(status().isOk())
            .andDo(
            document(
                "user/application-getApplication",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
    @Nested
    @DisplayName("신청서 승인 테스트")
    class 신청서승인{
        @Test
        @DisplayName("신청서 승인 테스트 성공")
        void 신청서승인1 () throws Exception {
            //given
            ApplicationCheckReq req = ApplicationCheckReq.builder().applicationId(TEST_APPLICATION_ID).accept(ANY_STRING).build();
            ApplicationConfirmRes res = ApplicationConfirmRes.builder().applicationId(TEST_APPLICATION_ID).email(TEST_EMAIL).message("해당 신청이 승인/거절 되었습니다").build();
            given(applicationService.confirm(req)).willReturn(res);
            //when
            ResultActions actions = mockMvc.perform(
                patch("/api/" + versionConfig.getVersion() + "/users/application")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)));
            //then
            actions.andExpect(status().isOk())
                .andDo(
                    document(
                        "user/application-confirm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        }
        @Test
        @DisplayName("신청서 승인 테스트 실패")
        void 신청서승인2 () throws Exception {
            //given
            ApplicationCheckReq req = ApplicationCheckReq.builder().build();
            //when
            ResultActions actions = mockMvc.perform(
                patch("/api/" + versionConfig.getVersion() + "/users/application")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)));
            //then
            actions.andExpect(status().isBadRequest());
        }
    }
}