package com.b6.mypaldotrip.domain.user.controller.dto.request;

import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.domain.user.store.entity.UserSort;

public record UserListReq(
        int page,
        int size,
        boolean asc,
        UserSort userSort,
        Long ageCondition,
        Long levelCondition,
        UserRole userRoleCondition,
        Boolean followerCondition,
        Boolean followingCondition) {}
