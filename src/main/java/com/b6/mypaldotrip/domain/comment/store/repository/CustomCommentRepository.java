package com.b6.mypaldotrip.domain.comment.store.repository;

import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCommentRepository {

    Page<CommentEntity> getCommentListByDynamicConditions(
            Long courseId,
            Pageable pageable,
            CommentSort commentSort,
            Long userId,
            Boolean filterByFollowing);
}
