package com.b6.mypaldotrip.domain.follow.service;

import com.b6.mypaldotrip.domain.follow.controller.dto.response.FollowToggleRes;
import com.b6.mypaldotrip.domain.follow.exception.FollowErrorCode;
import com.b6.mypaldotrip.domain.follow.store.entity.FollowerEntity;
import com.b6.mypaldotrip.domain.follow.store.entity.FollowingEntity;
import com.b6.mypaldotrip.domain.follow.store.repository.FollowerRepository;
import com.b6.mypaldotrip.domain.follow.store.repository.FollowingRepository;
import com.b6.mypaldotrip.domain.user.service.UserService;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowerRepository followerRepository;
    private final FollowingRepository followingRepository;
    private final UserService userService;

    public FollowToggleRes followToggle(Long followedUserId, Long followingUserId) {
        if (Objects.equals(followedUserId, followingUserId)) {
            throw new GlobalException(FollowErrorCode.SAME_USER_ID);
        }
        return getFollowToggleRes(followedUserId, followingUserId);
    }

    private FollowToggleRes getFollowToggleRes(Long followedUserId, Long followingUserId) {
        Optional<FollowerEntity> follower =
                followerRepository.findByUserIdAndFollowedId(followedUserId, followingUserId);
        Optional<FollowingEntity> following =
                followingRepository.findByUseIdAndFollowingId(followingUserId, followedUserId);

        if (follower.isPresent() && following.isPresent()) {
            followerRepository.delete(follower.get());
            followingRepository.delete(following.get());
            return FollowToggleRes.builder().message("팔로우 취소 완료").build();
        } else {
            UserEntity followedUser = userService.findUser(followedUserId);
            UserEntity followingUser = userService.findUser(followingUserId);
            followerRepository.save(
                    FollowerEntity.builder()
                            .user(followedUser)
                            .followedUser(followingUser)
                            .build());
            followingRepository.save(
                    FollowingEntity.builder()
                            .user(followingUser)
                            .followingUser(followedUser)
                            .build());
            return FollowToggleRes.builder().message("팔로우 완료").build();
        }
    }
}
