package com.b6.mypaldotrip.domain.course.store.entity;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import com.b6.mypaldotrip.domain.courseFile.store.entity.CourseFileEntity;
import com.b6.mypaldotrip.domain.like.store.entity.LikeEntity;
import com.b6.mypaldotrip.domain.tripCourse.store.entity.TripCourseEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_course")
public class CourseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String title;

    private String content;

    private String thumbnailUrl;

    @Builder
    private CourseEntity(
            String title,
            String content,
            UserEntity userEntity,
            CityEntity cityEntity,
            String thumbnailUrl) {
        this.userEntity = userEntity;
        this.cityEntity = cityEntity;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
    }

    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeEntity> likes = new ArrayList<>();

    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity cityEntity;

    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseFileEntity> files = new ArrayList<>();

    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripCourseEntity> tripCourses = new ArrayList<>();

    public void updateCourse(String title, String content, CityEntity cityEntity) {
        this.title = title;
        this.content = content;
        this.cityEntity = cityEntity;
    }

    public void clearTripCourses() {
        this.tripCourses.clear();
    }

    public void updateTripCourses(TripCourseEntity tripCourseEntity) {
        this.tripCourses.add(tripCourseEntity);
    }
}
