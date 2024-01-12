package com.b6.mypaldotrip.domain.review.controller.dto.request;

import com.b6.mypaldotrip.domain.review.store.entity.ReviewSort;

public record ReviewListReq(ReviewSort reviewSort, int page) {}
