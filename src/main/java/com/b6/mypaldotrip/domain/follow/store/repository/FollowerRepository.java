package com.b6.mypaldotrip.domain.follow.store.repository;

import com.b6.mypaldotrip.domain.follow.store.entity.FollowerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowerRepository extends JpaRepository<FollowerEntity, Long> {

    @Query(
            "select f from FollowerEntity f where f.user.userId =:targetUserId and f.followedUser.userId =:loggedUserId")
    Optional<FollowerEntity> findByUserIdAndFollowedId(Long targetUserId, Long loggedUserId);
}
