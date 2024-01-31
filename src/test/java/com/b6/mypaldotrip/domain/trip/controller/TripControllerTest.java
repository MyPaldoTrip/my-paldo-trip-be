package com.b6.mypaldotrip.domain.trip.controller;

import com.b6.mypaldotrip.domain.trip.TripTestUtils;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripCreateRes;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.Category;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {TripController.class})
@MockBean(JpaMetamodelMappingContext.class)
class TripControllerTest extends TripTestUtils {

    @MockBean private TripService tripService;

    @Nested
    @DisplayName("여행정보 생성 테스트")
    class 여행정보_생성 {

        @Test
        @DisplayName("여행정보 생성 성공")
        void 여행정보_생성1() throws Exception {
            // given
            TripCreateReq req =
                    TripCreateReq.builder()
                            .cityName(TEST_CITY_NAME)
                            .category(Category.ATTRACTION)
                            .name(TEST_TRIP_NAME)
                            .description(TEST_DESCRIPTION)
                            .build();
            TripCreateRes res =
                    TripCreateRes.builder()
                            .city(TEST_CITY_NAME)
                            .category(Category.ATTRACTION)
                            .name(TEST_TRIP_NAME)
                            .description(TEST_DESCRIPTION)
                            .tripFileList(Collections.emptyList())
                            .build();
            given(
                            tripService.createTrip(
                                    anyString(),
                                    any(MultipartFile.class),
                                    any(UserDetailsImpl.class)))
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
                                    multipart("/api/" + versionConfig.getVersion() + "/trips")
                                            .file(multipartFile)
                                            .file(jsonReq)
                                            .characterEncoding("utf-8")
                                            .contentType(MediaType.MULTIPART_FORM_DATA));

            // then
            actions.andExpect(status().isCreated())
                    .andDo(
                            document(
                                    "trip/createTrip",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())));
        }

        @Test
        @DisplayName("여행정보 생성 실패 - 파일 미첨부")
        void 여행정보_생성2() throws Exception {
            // given
            TripCreateReq req = TripCreateReq.builder().build();
            TripCreateRes res =
                    TripCreateRes.builder()
                            .city(TEST_CITY_NAME)
                            .category(Category.ATTRACTION)
                            .name(TEST_TRIP_NAME)
                            .description(TEST_DESCRIPTION)
                            .tripFileList(Collections.emptyList())
                            .build();
            given(tripService.createTrip(anyString(),
                    any(MultipartFile.class),
                    any(UserDetailsImpl.class)))
                    .willReturn(res);

            // when
            MockMultipartFile jsonReq =
                    new MockMultipartFile(
                            "req",
                            "",
                            "application/json",
                            objectMapper.writeValueAsString(req).getBytes());
            ResultActions actions =
                    mockMvc.perform(
                                    multipart("/api/" + versionConfig.getVersion() + "/trips")
                                            .file(jsonReq)
                                            .contentType(MediaType.MULTIPART_FORM_DATA));

            // then
            actions.andExpect(status().isBadRequest());
        }
    }
}
