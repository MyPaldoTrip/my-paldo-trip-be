package com.b6.mypaldotrip.domain.user.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user")
public class UserEntity {

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
    private String profileURL;
    private Long age;
    private Long level;

    @Builder
    private UserEntity(
            String email,
            String username,
            String password,
            String introduction,
            String profileURL,
            Long age,
            Long level) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.introduction = introduction;
        this.profileURL = profileURL;
        this.age = age;
        this.level = level;
    }
}
