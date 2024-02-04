package com.b6.mypaldotrip.domain.course.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.course.BaseCourseTest;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseUpdateReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseDeleteRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseGetRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseListRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseUpdateRes;
import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.domain.course.store.entity.CourseListWrapper;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.time.LocalDateTime;
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

@WebMvcTest(controllers = {CourseController.class})
@MockBean(JpaMetamodelMappingContext.class)
class CourseControllerTest extends BaseCourseTest {

    @MockBean private CourseService courseService;

    @Test
    @DisplayName("코스 생성")
    void 코스생성성공() throws Exception {
        // given

        CourseSaveReq req =
                CourseSaveReq.builder()
                        .title(TEST_COURSE_TITLE)
                        .content(TEST_COURSE_CONTENT)
                        .cityName("TestCity")
                        .build();
        CourseSaveRes res =
                CourseSaveRes.builder()
                        .courseId(1L)
                        .title(TEST_COURSE_TITLE)
                        .content(TEST_COURSE_CONTENT)
                        .build();
        String reqStr = objectMapper.writeValueAsString(req);
        given(courseService.saveCourse(any(), any(), any())).willReturn(res);

        MockMultipartFile reqPart =
                new MockMultipartFile("req", "", "text/plain", reqStr.getBytes());

        // when
        ResultActions actions =
                mockMvc.perform(
                        multipart("/api/" + versionConfig.getVersion() + "/courses")
                                .file(TEST_FILE)
                                .file(reqPart));

        // then
        actions.andExpect(status().isCreated())
                .andDo(
                        document(
                                "course/create",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }

    @Nested
    @DisplayName("코스 조회")
    class 조회 {

        @Test
        @DisplayName("목록조회")
        void 목록조회성공() throws Exception {
            // given
            int page = 1;
            int size = 10;
            CourseSearchReq req = CourseSearchReq.builder().build();
            String reqStr = objectMapper.writeValueAsString(req);
            List<CourseListRes> res =
                    List.of(
                            CourseListRes.builder()
                                    .courseId(1L)
                                    .title(TEST_COURSE_TITLE)
                                    .content(TEST_COURSE_CONTENT)
                                    .username(TEST_USERNAME)
                                    .thumbnailUrl(TEST_COURSE_THUMBNAIL_URL)
                                    .likeCount(0)
                                    .createdAt(LocalDateTime.now())
                                    .totalPage(1)
                                    .level(1L)
                                    .commentCount(0)
                                    .build());
            CourseListWrapper courseListWrapper = CourseListWrapper.builder().courseListResList(res).build();

            given(
                            courseService.getCourseListByDynamicConditions(
                                    anyInt(),
                                    anyInt(),
                                    any(CourseSearchReq.class),
                                    any(UserDetailsImpl.class)))
                    .willReturn(courseListWrapper);

            // when
            ResultActions actions =
                    mockMvc.perform(
                            post("/api/" + versionConfig.getVersion() + "/courses/list")
                                    .param("page", String.valueOf(page))
                                    .param("size", String.valueOf(size))
                                    .content(reqStr)
                                    .contentType(MediaType.APPLICATION_JSON));

            // then
            actions.andExpect(status().isOk())
                    .andDo(
                            document(
                                    "course/getCourseList",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())));
        }

        @Test
        @DisplayName("단건 조회")
        void 단건조회성공() throws Exception {
            // given
            CourseGetRes res =
                    CourseGetRes.builder()
                            .courseId(1L)
                            .cityName("relatedCityName")
                            .createdAt(LocalDateTime.now())
                            .username(TEST_USERNAME)
                            .title(TEST_COURSE_TITLE)
                            .content(TEST_COURSE_CONTENT)
                            .fileURL(List.of(TEST_FILE_URL))
                            .relatedTripIds(List.of(1L))
                            .relatedTripNames(List.of("testTripName"))
                            .build();
            given(courseService.getCourse(anyLong())).willReturn(res);
            // when
            ResultActions actions =
                    mockMvc.perform(get("/api/" + versionConfig.getVersion() + "/courses/1"));
            // then
            actions.andExpect(status().isOk())
                    .andDo(
                            document(
                                    "course/getCourse",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())));
        }
    }

    @Nested
    @DisplayName("코스 수정 테스트")
    class 코스수정 {

        @Test
        @DisplayName("코스 수정 성공")
        void 수정성공() throws Exception {
            // given
            CourseUpdateReq req =
                    CourseUpdateReq.builder()
                            .title("updated " + TEST_COURSE_TITLE)
                            .content("updated " + TEST_COURSE_CONTENT)
                            .cityName("another TestCity")
                            .tripNames(List.of("updated tripNames"))
                            .build();
            CourseUpdateRes res =
                    CourseUpdateRes.builder()
                            .title("updated " + TEST_COURSE_TITLE)
                            .content("updated " + TEST_COURSE_CONTENT)
                            .build();
            given(
                            courseService.updateCourse(
                                    anyLong(), any(CourseUpdateReq.class), any(UserEntity.class)))
                    .willReturn(res);
            // when
            ResultActions actions =
                    mockMvc.perform(
                            put("/api/" + versionConfig.getVersion() + "/courses/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .characterEncoding("utf-8")
                                    .content(objectMapper.writeValueAsString(req)));
            // then
            actions.andExpect(status().isOk())
                    .andDo(
                            document(
                                    "course/updateCourse",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())));
        }

        @Test
        @DisplayName("코스 수정 실패 - 파라미터 X")
        void 수정실패() throws Exception {
            // given

            // when
            ResultActions actions =
                    mockMvc.perform(
                            put("/api/" + versionConfig.getVersion() + "/courses/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .characterEncoding("utf-8")
                                    .content(""));
            // then
            actions.andExpect(status().isBadRequest()).andDo(print());
        }
    }

    @Test
    @DisplayName("코스 삭제")
    void 코스삭제성공() throws Exception {
        // given
        CourseDeleteRes res = CourseDeleteRes.builder().msg("1 번 코스 삭제, 삭제할 파일 없음").build();
        given(courseService.deleteCourse(anyLong(), any(UserEntity.class))).willReturn(res);
        // when
        ResultActions actions =
                mockMvc.perform(delete("/api/" + versionConfig.getVersion() + "/courses/1"));
        // then
        actions.andExpect(status().isOk())
                .andDo(
                        document(
                                "course/deleteCourse",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }
}
