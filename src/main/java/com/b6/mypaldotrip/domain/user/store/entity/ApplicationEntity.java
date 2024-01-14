package com.b6.mypaldotrip.domain.user.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_application")
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String title;

    private String content;

    private Boolean verified;

    @Builder
    private ApplicationEntity(UserEntity userEntity, String title, String content) {
        this.userEntity = userEntity;
        this.title = title;
        this.content = content;
        this.verified = false;
    }
}
