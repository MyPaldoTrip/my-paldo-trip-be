package com.b6.mypaldotrip.domain.user.store.repository;

import com.b6.mypaldotrip.domain.user.controller.dto.response.UserGetMyProfileRes;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = UserEntity.class, idClass = Long.class)
public interface UserRepository extends JpaRepository<UserEntity, Long>, UserCustomRepository {

    Optional<UserEntity> findByEmail(String email);

    @Query(
            "select new com.b6.mypaldotrip.domain.user.controller.dto.response.UserGetMyProfileRes(u.userId,u.username,u.userRole) from UserEntity u where u.userId =:userId")
    UserGetMyProfileRes findNameAndRoleByUserId(Long userId);
}
