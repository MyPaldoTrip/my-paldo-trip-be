package com.b6.mypaldotrip.domain.user.store.repository;

import com.b6.mypaldotrip.domain.user.controller.dto.request.UserListReq;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCustomRepository {

    List<UserEntity> findByDynamicConditions(UserListReq req, UserDetailsImpl userDetails);
}
