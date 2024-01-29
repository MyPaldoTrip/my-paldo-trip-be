package com.b6.mypaldotrip.domain.user.controller.dto.response;

import com.b6.mypaldotrip.domain.follow.controller.dto.response.FollowRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewListRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserGetProfileRes(
        Long userId,
        String email,
        String username,
        String introduction,
        String profileURL,
        Long age,
        Long level,
        List<ReviewListRes> reviewListResList,
        List<FollowRes> followingEntityList,
        List<FollowRes> followerEntityList) {}
