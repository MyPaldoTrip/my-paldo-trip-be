package com.b6.mypaldotrip.domain.course.controller;

import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
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
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/${mpt.version}/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<CourseSaveRes>> saveCourse(
            @Valid @RequestPart CourseSaveReq req,
            @RequestPart MultipartFile multipartFile,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws IOException {
        CourseSaveRes res =
                courseService.saveCourse(req, userDetails.getUserEntity(), multipartFile);

        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<CourseListRes>>> getCourseListByDynamicConditions(
            @RequestParam int page,
            @RequestParam int size,
            @RequestBody CourseSearchReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails != null ? userDetails.getUserEntity().getUserId() : null;

        List<CourseListRes> res =
                courseService.getCourseListByDynamicConditions(page, size, req, userId);

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
            @PathVariable Long courseId,
            @RequestBody CourseUpdateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CourseUpdateRes res =
                courseService.updateCourse(courseId, req, userDetails.getUserEntity());

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<RestResponse<CourseDeleteRes>> deleteCourse(
            @PathVariable Long courseId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CourseDeleteRes res = courseService.deleteCourse(courseId, userDetails.getUserEntity());

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
