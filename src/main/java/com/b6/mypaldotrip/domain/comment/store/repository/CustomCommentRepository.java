package com.b6.mypaldotrip.domain.comment.store.repository;

import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentSort;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCommentRepository {

    List<CommentEntity> getCommentListByDynamicConditions(
            Long courseId,
            Pageable pageable,
            CommentSort commentSort,
            Long userId,
            Boolean filterByFollowing);
}
