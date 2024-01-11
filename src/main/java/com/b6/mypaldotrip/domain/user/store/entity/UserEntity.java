package com.b6.mypaldotrip.domain.user.store.entity;

import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.global.common.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String introduction;
    private String fileURL;
    private Long age;
    private Long level;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviewList = new ArrayList<>();

    @Builder
    private UserEntity(
            String email,
            String username,
            String password,
            String introduction,
            String fileURL,
            Long age,
            Long level) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.introduction = introduction;
        this.fileURL = fileURL;
        this.age = age;
        this.level = level;
    }

    public void update(
            String username, String introduction, Long age, String password, String fileURL) {
        this.username = username;
        this.introduction = introduction;
        this.age = age;
        this.password = password;
        this.fileURL = fileURL;
    }
}
