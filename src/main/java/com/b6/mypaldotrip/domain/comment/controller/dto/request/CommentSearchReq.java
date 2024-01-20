package com.b6.mypaldotrip.domain.comment.controller.dto.request;

import com.b6.mypaldotrip.domain.comment.store.entity.CommentSort;

public record CommentSearchReq(Boolean filterByFollowing, CommentSort commentSort) {}
