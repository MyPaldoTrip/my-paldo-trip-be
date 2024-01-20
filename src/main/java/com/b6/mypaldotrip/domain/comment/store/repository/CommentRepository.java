package com.b6.mypaldotrip.domain.comment.store.repository;

import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository
        extends JpaRepository<CommentEntity, Long>, CustomCommentRepository {}
