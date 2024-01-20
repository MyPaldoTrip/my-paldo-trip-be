package com.b6.mypaldotrip.domain.courseFile.service;

import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.courseFile.controller.dto.response.CourseFileAddRes;
import com.b6.mypaldotrip.domain.courseFile.controller.dto.response.CourseFileDeleteRes;
import com.b6.mypaldotrip.domain.courseFile.controller.dto.response.CourseFileListRes;
import com.b6.mypaldotrip.domain.courseFile.exception.CourseFileErrorCode;
import com.b6.mypaldotrip.domain.courseFile.store.entity.CourseFileEntity;
import com.b6.mypaldotrip.domain.courseFile.store.repository.CourseFileRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
    public CourseFileAddRes addFiles(Long courseId, MultipartFile multipartFile, UserEntity userEntity)
            throws IOException {
        CourseEntity courseEntity = findCourse(courseId);
        validateAuth(userEntity, courseEntity);

        CourseFileEntity courseFileEntity =
                CourseFileEntity.builder().courseEntity(courseEntity).build();

        String fileUrl;
        try {
            fileUrl = s3Provider.updateFile(courseFileEntity, multipartFile);
        } catch (GlobalException e) {
            fileUrl = s3Provider.saveFile(multipartFile, "course");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        courseFileEntity =
                CourseFileEntity.builder().courseEntity(courseEntity).fileURL(fileUrl).build();

        courseFileRepository.save(courseFileEntity);

        return CourseFileAddRes.builder().msg("파일이 성공적으로 업로드되었습니다.").build();
    }



    public List<CourseFileListRes> getFileList(Long courseId, UserEntity userEntity) {
        CourseEntity courseEntity = findCourse(courseId);
        validateAuth(userEntity,courseEntity);

        List<CourseFileListRes> res =
                courseFileRepository.findAllByCourseEntity(courseEntity).stream()
                        .map(
                                courseFile ->
                                        CourseFileListRes.builder()
                                                .CourseFileId(courseFile.getCourseFileId())
                                                .FileURL(courseFile.getFileURL())
                                                .build())
                        .toList();

        return res;
    }

    public CourseFileDeleteRes deleteFile(Long courseId, Long fileId, UserEntity userEntity) {
        CourseEntity courseEntity = findCourse(courseId);
        validateAuth(userEntity, courseEntity);

        CourseFileEntity courseFileEntity =
                courseFileRepository
                        .findById(fileId)
                        .orElseThrow(() -> new GlobalException(CourseFileErrorCode.FILE_NOT_FOUND));

        courseFileRepository.delete(courseFileEntity);

        s3Provider.deleteFile(courseFileEntity);

        CourseFileDeleteRes res =
                CourseFileDeleteRes.builder()
                        .courseFileId(courseFileEntity.getCourseFileId())
                        .msg("파일이 삭제 되었습니다")
                        .build();

        return res;
    }

    private CourseEntity findCourse(Long courseId) {
        return courseService.findCourse(courseId);
    }

    private static void validateAuth(UserEntity userEntity, CourseEntity courseEntity) {
        if (!Objects.equals(courseEntity.getUserEntity().getUserId(), userEntity.getUserId())) {
            throw new GlobalException(CourseFileErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
