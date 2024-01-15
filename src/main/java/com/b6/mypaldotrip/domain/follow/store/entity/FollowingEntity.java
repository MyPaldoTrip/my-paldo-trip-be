package com.b6.mypaldotrip.domain.follow.store.entity;

import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
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
@Table(name = "tb_following")
public class FollowingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id")
    private UserEntity followingUser;

    @Builder
    private FollowingEntity(UserEntity user, UserEntity followingUser) {
        this.user = user;
        this.followingUser = followingUser;
    }
}
