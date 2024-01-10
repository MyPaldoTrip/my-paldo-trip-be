package com.b6.mypaldotrip.domain.course.controller;

import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseUpdateReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseDeleteRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseGetRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseListRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseUpdateRes;
import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${mpt.version}/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<CourseSaveRes>> saveCourse(
            @Valid @RequestBody CourseSaveReq req) {
        CourseSaveRes res = courseService.saveCourse(req);

        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<CourseListRes>>> getCourseList() {
        List<CourseListRes> res = courseService.getCourseList();

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<RestResponse<CourseGetRes>> getCourse(@PathVariable Long courseId) {
        CourseGetRes res = courseService.getCourse(courseId);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PutMapping("{courseId}")
    public ResponseEntity<RestResponse<CourseUpdateRes>> updateCourse(
            @PathVariable Long courseId, @RequestBody CourseUpdateReq req) {
        CourseUpdateRes res = courseService.updateCourse(courseId, req);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<RestResponse<CourseDeleteRes>> deleteCourse(@PathVariable Long courseId) {
        CourseDeleteRes res = courseService.deleteCourse(courseId);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
