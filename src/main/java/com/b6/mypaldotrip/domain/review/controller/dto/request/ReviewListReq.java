package com.b6.mypaldotrip.domain.review.controller.dto.request;

import com.b6.mypaldotrip.domain.review.store.entity.ReviewSort;

public record ReviewListReq(boolean filterByFollowing, ReviewSort reviewSort, int page) {}
