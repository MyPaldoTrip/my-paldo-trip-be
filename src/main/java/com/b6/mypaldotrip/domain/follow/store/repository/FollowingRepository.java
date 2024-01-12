package com.b6.mypaldotrip.domain.follow.store.repository;

import com.b6.mypaldotrip.domain.follow.store.entity.FollowingEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowingRepository extends JpaRepository<FollowingEntity, Long> {

    @Query(
            "select f from FollowingEntity f where f.user.userId =:followingUserId and f.followingUser.userId =:followedUserId")
    Optional<FollowingEntity> findByUseIdAndFollowingId(Long followingUserId, Long followedUserId);
}
