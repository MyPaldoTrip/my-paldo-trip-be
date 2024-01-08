package com.b6.mypaldotrip.domain.course.controller;

import com.b6.mypaldotrip.domain.course.controller.dto.requeset.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<RestResponse<CourseSaveRes>> saveCourse(@Valid @RequestBody CourseSaveReq req) {
        CourseSaveRes res = courseService.saveCourse(req);
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
            .toResponseEntity();
    }

}
