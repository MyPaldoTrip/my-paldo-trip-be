package com.b6.mypaldotrip.domain.user.store.entity;

import com.b6.mypaldotrip.domain.follow.store.entity.FollowerEntity;
import com.b6.mypaldotrip.domain.follow.store.entity.FollowingEntity;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FollowerEntity> followerList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FollowingEntity> followingList = new ArrayList<>();

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
        this.userRole = UserRole.ROLE_USER;
    }

    public void update(
            String username, String introduction, Long age, String password, String fileURL) {
        this.username = username;
        this.introduction = introduction;
        this.age = age;
        this.password = password;
        this.fileURL = fileURL;
    }

    public void acceptPermission() {
        this.userRole = UserRole.ROLE_OPERATOR;
    }

    public void reject() {
        this.userRole = UserRole.ROLE_USER;
    }
}
