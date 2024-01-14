package com.b6.mypaldotrip.domain.comment.store.entity;

import com.b6.mypaldotrip.domain.comment.exception.CommentErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum CommentSort {
    MODIFIED,
    LEVEL;

    @JsonCreator
    public static CommentSort forValue(String value) {
        for (CommentSort commentSort : CommentSort.values()) {
            if (commentSort.name().equalsIgnoreCase(value)) {
                return commentSort;
            }
        }
        throw new GlobalException(CommentErrorCode.WRONG_COMMENT_SORT);
    }
}
