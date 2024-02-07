package com.b6.mypaldotrip.domain.city.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.city.controller.dto.request.CityCreateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.CityUpdateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityCreateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityDeleteRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityUpdateRes;
import com.b6.mypaldotrip.domain.city.s3.service.CityFileService;
import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.city.service.CityTestUtils;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(controllers = {CityController.class})
@MockBean(JpaMetamodelMappingContext.class)
public class CityControllerTest extends CityTestUtils {

    @MockBean private CityService cityService;
    @MockBean private CityFileService cityFileService;

    @Nested
    @DisplayName("도시 생성 테스트")
    class 도시정보_테스트 {

        @Test
        @DisplayName("도시 생성 성공")
        void 여행정보_생성1() throws Exception {
            // given
            CityCreateReq req =
                    CityCreateReq.builder()
                            .provinceName(TEST_PROVINCE_NAME)
                            .cityName(TEST_CITY_NAME)
                            .cityInfo(TEST_CITY_INFO)
                            .build();
            CityCreateRes res =
                    CityCreateRes.builder()
                            .provinceName(TEST_PROVINCE_NAME)
                            .cityName(TEST_CITY_NAME)
                            .cityInfo(TEST_CITY_INFO)
                            .build();
            given(
                            cityService.createCity(
                                    anyString(),
                                    any(UserDetailsImpl.class),
                                    any(MultipartFile.class)))
                    .willReturn(res);
            // when
            MockMultipartFile multipartFile =
                    new MockMultipartFile(
                            "multipartFile", "test.txt", "text/plain", "test data".getBytes());
            MockMultipartFile jsonReq =
                    new MockMultipartFile(
                            "req",
                            "",
                            "application/json",
                            objectMapper.writeValueAsString(req).getBytes());
            ResultActions actions =
                    mockMvc.perform(
                            multipart("/api/" + versionConfig.getVersion() + "/cities")
                                    .file(multipartFile)
                                    .file(jsonReq)
                                    .characterEncoding("utf-8")
                                    .contentType(MediaType.MULTIPART_FORM_DATA));

            // then
            actions.andExpect(status().isCreated())
                    .andDo(
                            document(
                                    "city/createCity",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())));
        }

        @Test
        @DisplayName("도시 생성 실패 - 요청 파라미터 없음")
        void 도시정보_생성2() throws Exception {
            // given

            // when
            ResultActions actions =
                    mockMvc.perform(
                            multipart("/api/" + versionConfig.getVersion() + "/cities")
                                    .characterEncoding("utf-8")
                                    .contentType(MediaType.MULTIPART_FORM_DATA));

            // then
            actions.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("도시 수정 테스트 성공")
        void 도시_수정() throws Exception {
            // given
            CityUpdateReq req =
                    CityUpdateReq.builder()
                            .provinceName(TEST_PROVINCE_NAME)
                            .cityName(TEST_CITY_NAME)
                            .cityInfo(TEST_CITY_INFO)
                            .build();

            CityUpdateRes res =
                    CityUpdateRes.builder()
                            .provinceName(TEST_PROVINCE_NAME)
                            .cityName(TEST_CITY_NAME)
                            .cityInfo(TEST_CITY_INFO)
                            .build();
            given(cityService.updateCity(any(), any(), any())).willReturn(res);

            // when
            ResultActions actions =
                    mockMvc.perform(
                            put(
                                            "/api/"
                                                    + versionConfig.getVersion()
                                                    + "/cities/{cityId}",
                                            TEST_CITYID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .characterEncoding("utf-8")
                                    .content(objectMapper.writeValueAsString(req)));

            // then
            actions.andExpect(status().isOk())
                    .andDo(
                            document(
                                    "city/updateCity",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())));
        }

        @Test
        @DisplayName("도시 삭제 테스트 성공")
        void 도시_삭제() throws Exception {
            // given
            CityDeleteRes res = CityDeleteRes.builder().msg("삭제 완료 메세지").build();
            given(cityService.deleteCity(any(), any())).willReturn(res);

            // when
            ResultActions actions =
                    mockMvc.perform(
                            delete(
                                    "/api/" + versionConfig.getVersion() + "/cities/{cityId}",
                                    TEST_CITYID));

            // then
            actions.andExpect(status().isOk())
                    .andDo(
                            document(
                                    "city/deleteCity",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())));
        }
    }
}
