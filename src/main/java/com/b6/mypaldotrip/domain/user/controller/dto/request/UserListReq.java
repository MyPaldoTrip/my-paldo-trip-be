package com.b6.mypaldotrip.domain.user.controller.dto.request;

import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.domain.user.store.entity.UserSort;
import lombok.Builder;

@Builder
public record UserListReq(
        int page,
        int size,
        Boolean asc,
        UserSort userSort,
        Long ageCondition,
        Long levelCondition,
        UserRole userRoleCondition,
        Boolean followerCondition,
        Boolean followingCondition) {}
