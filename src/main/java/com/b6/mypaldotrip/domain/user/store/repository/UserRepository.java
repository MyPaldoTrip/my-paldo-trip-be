package com.b6.mypaldotrip.domain.user.store.repository;

import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
