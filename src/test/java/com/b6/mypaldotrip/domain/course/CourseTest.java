package com.b6.mypaldotrip.domain.course;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.user.CommonTest;
import org.springframework.mock.web.MockMultipartFile;

public interface CourseTest extends CommonTest {

    String TEST_COURSE_TITLE = "testName";
    String TEST_COURSE_CONTENT = "testContent";
    String TEST_COURSE_THUMBNAIL_URL = "testUrl";
    String ANOTHER = "another";
    byte[] imageBytes = "Test Image Content".getBytes();
    MockMultipartFile TEST_FILE =
            new MockMultipartFile("multipartFile", "test.jpg", "image/jpeg", imageBytes);
    CourseEntity TEST_COURSE =
            CourseEntity.builder()
                    .userEntity(TEST_USER)
                    .cityEntity(CityEntity.builder().build())
                    .title(TEST_COURSE_TITLE)
                    .content(TEST_COURSE_CONTENT)
                    .thumbnailUrl(TEST_COURSE_THUMBNAIL_URL)
                    .build();

    CourseEntity ANOTHER_COURSE =
            CourseEntity.builder()
                    .userEntity(TEST_ANOTHER_USER)
                    .cityEntity(CityEntity.builder().build())
                    .title(ANOTHER + TEST_COURSE_TITLE)
                    .content(ANOTHER + TEST_COURSE_CONTENT)
                    .thumbnailUrl(ANOTHER + TEST_COURSE_THUMBNAIL_URL)
                    .build();
}
