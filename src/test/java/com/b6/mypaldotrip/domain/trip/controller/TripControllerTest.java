package com.b6.mypaldotrip.domain.trip.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.trip.TripTestUtils;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripListReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripUpdateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripCreateRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripDeleteRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripGetRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripListRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripListWrapper;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripUpdateRes;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        @DisplayName("여행정보 생성 실패 - 요청 파라미터 없음")
        void 여행정보_생성3() throws Exception {
            // given

            // when
            ResultActions actions =
                    mockMvc.perform(
                            multipart("/api/" + versionConfig.getVersion() + "/trips")
                                    .characterEncoding("utf-8")
                                    .contentType(MediaType.MULTIPART_FORM_DATA));

            // then
            actions.andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("여행정보 목록 조회 성공")
    void 여행정보_목록조회() throws Exception {
        // given
        TripListReq req = TripListReq.builder().build();
        List<TripListRes> res =
                Arrays.asList(
                        TripListRes.builder()
                                .tripId(1L)
                                .city(TEST_CITY_NAME)
                                .category(Category.ATTRACTION)
                                .name(TEST_TRIP_NAME)
                                .averageRating(5.0)
                                .reviews(100)
                                .fileUrlList(Collections.emptyList())
                                .build());
        TripListWrapper wrapper = TripListWrapper.builder().tripListRes(res).build();
        given(tripService.getTripList(any(TripListReq.class))).willReturn(wrapper);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/api/" + versionConfig.getVersion() + "/trips/lists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)));

        // then
        actions.andExpect(status().isOk())
                .andDo(
                        document(
                                "trip/getTripList",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("여행정보 단건 조회 성공")
    void 여행정보_단건조회() throws Exception {
        // given
        TripGetRes res =
                TripGetRes.builder()
                        .tripId(1L)
                        .city(TEST_CITY_NAME)
                        .category(Category.ATTRACTION)
                        .description(TEST_DESCRIPTION)
                        .urlList(Collections.emptyList())
                        .build();
        given(tripService.getTrip(any())).willReturn(res);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/api/" + versionConfig.getVersion() + "/trips/{tripId}", TEST_TRIPID));

        // then
        actions.andExpect(status().isOk())
                .andDo(
                        document(
                                "trip/getTrip",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("여행정보 수정 테스트 성공")
    void 여행정보_수정() throws Exception {
        // given
        TripUpdateReq req =
                TripUpdateReq.builder()
                        .cityName(TEST_CITY_NAME)
                        .category(Category.ATTRACTION)
                        .name(TEST_TRIP_NAME)
                        .description(TEST_DESCRIPTION)
                        .build();
        TripUpdateRes res =
                TripUpdateRes.builder()
                        .city(TEST_CITY_NAME)
                        .category(Category.ATTRACTION)
                        .name(TEST_TRIP_NAME)
                        .description(TEST_DESCRIPTION)
                        .build();
        given(tripService.updateTrip(any(), any(), any())).willReturn(res);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch("/api/" + versionConfig.getVersion() + "/trips/{tripId}", TEST_TRIPID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(req)));

        // then
        actions.andExpect(status().isOk())
                .andDo(
                        document(
                                "trip/updateTrip",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("여행정보 삭제 테스트 성공")
    void 여행정보_삭제() throws Exception {
        // given
        TripDeleteRes res = TripDeleteRes.builder().message("삭제 완료 메세지").build();
        given(tripService.deleteTrip(any(), any())).willReturn(res);

        // when
        ResultActions actions =
                mockMvc.perform(
                        delete(
                                "/api/" + versionConfig.getVersion() + "/trips/{tripId}",
                                TEST_TRIPID));

        // then
        actions.andExpect(status().isOk())
                .andDo(
                        document(
                                "trip/deleteTrip",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }
}
