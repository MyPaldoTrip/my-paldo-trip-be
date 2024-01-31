package com.b6.mypaldotrip.domain.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.user.CommonControllerTest;
import com.b6.mypaldotrip.domain.user.controller.dto.request.EmailSendReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.EmailVerifyReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.EmailSendRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.EmailVerifyRes;
import com.b6.mypaldotrip.domain.user.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = {EmailController.class})
@MockBean(JpaMetamodelMappingContext.class)
class EmailControllerTest extends CommonControllerTest {

    @MockBean private EmailService emailService;

    @Nested
    @DisplayName("이메일 발송 테스트")
    class 이메일발송{
        @Test
        @DisplayName("이메일 발송 테스트 성공")
        void 이메일발송1 () throws Exception {
            //given
            EmailSendReq req = EmailSendReq.builder().email(TEST_EMAIL).build();
            EmailSendRes res = EmailSendRes.builder().recipientEmail(TEST_EMAIL).message("인증 코드 발송").build();
            given(emailService.sendEmail(req)).willReturn(res);

            //when
            ResultActions actions = mockMvc.perform(
                post("/api/" + versionConfig.getVersion() + "/users/email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)));

            //then
            actions.andExpect(status().isOk())
                .andDo(
                    document(
                        "user/email-send",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        }
        @Test
        @DisplayName("이메일 발송 테스트 실패")
        void 이메일발송2 () throws Exception {
            //given
            EmailSendReq req = EmailSendReq.builder().email("test").build();
            //when
            ResultActions actions = mockMvc.perform(
                post("/api/" + versionConfig.getVersion() + "/users/email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)));
            //then
            actions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("이메일 검증 테스트")
    class 이메일검증{
        @Test
        @DisplayName("이메일 검증 테스트 성공")
        void 이메일검증1 () throws Exception {
            //given
            EmailVerifyReq req = EmailVerifyReq.builder().email(TEST_EMAIL).code("testCode").build();
            EmailVerifyRes res = EmailVerifyRes.builder().message("인증 코드 검증 성공").build();
            given(emailService.verifyEmail(req)).willReturn(res);
            //when
            ResultActions actions = mockMvc.perform(
                post("/api/" + versionConfig.getVersion() + "/users/email/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)));
            //then
            actions.andExpect(status().isOk())
                .andDo(
                    document(
                        "user/email-verify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        }
        @Test
        @DisplayName("이메일 검증 테스트 실패")
        void 이메일검증2 () throws Exception {
            //given
            EmailVerifyReq req = EmailVerifyReq.builder().build();
            //when
            ResultActions actions = mockMvc.perform(
                post("/api/" + versionConfig.getVersion() + "/users/email/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)));
            //then
            actions.andExpect(status().isBadRequest());
        }
    }
}