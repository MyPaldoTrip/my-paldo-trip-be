package com.b6.mypaldotrip.domain.course.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.course.BaseCourseTest;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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
}
