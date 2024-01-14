package com.b6.mypaldotrip.domain.courseFile.service;

import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.courseFile.controller.dto.response.CourseFileAddRes;
import com.b6.mypaldotrip.domain.courseFile.controller.dto.response.CourseFileListRes;
import com.b6.mypaldotrip.domain.courseFile.store.entity.CourseFileEntity;
import com.b6.mypaldotrip.domain.courseFile.store.repository.CourseFileRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CourseFileService {

    private final CourseService courseService;
    private final S3Provider s3Provider;
    private final CourseFileRepository courseFileRepository;

    @Transactional
    public CourseFileAddRes addFiles(Long courseId, MultipartFile multipartFile)
        throws IOException {

        CourseEntity courseEntity = courseService.findCourse(courseId);
        CourseFileEntity courseFileEntity = CourseFileEntity.builder().courseEntity(courseEntity)
            .build();
        String fileUrl;
        try {
            fileUrl = s3Provider.updateFile(courseFileEntity, multipartFile);
        } catch (GlobalException e) {
            fileUrl = s3Provider.saveFile(multipartFile, "course");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        courseFileEntity = CourseFileEntity.builder().courseEntity(courseEntity).fileURL(fileUrl)
            .build();

        courseFileRepository.save(courseFileEntity);

        return CourseFileAddRes.builder().msg("파일이 성공적으로 업로드되었습니다.").build();
    }

    public List<CourseFileListRes> getFileList(Long courseId) {
        CourseEntity courseEntity = courseService.findCourse(courseId);

        List<CourseFileListRes> res = courseFileRepository.findAllByCourseEntity(
                courseEntity).stream().map(
                courseFile -> CourseFileListRes.builder().CourseFileId(courseFile.getCourseFileId()).FileURL(courseFile.getFileURL()).build())
            .toList();

        return res;
    }
}
