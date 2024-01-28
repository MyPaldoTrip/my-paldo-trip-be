package com.b6.mypaldotrip.domain.user.store.repository;

import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = UserEntity.class, idClass = Long.class)
public interface UserRepository extends JpaRepository<UserEntity, Long>, UserCustomRepository {

    Optional<UserEntity> findByEmail(String email);

    @Query("select u.username from UserEntity u where u.userId =:userId")
    String findUsernameByUserId(Long userId);
}
