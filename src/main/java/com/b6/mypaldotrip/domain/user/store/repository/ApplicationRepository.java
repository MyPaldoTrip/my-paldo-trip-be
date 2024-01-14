package com.b6.mypaldotrip.domain.user.store.repository;

import com.b6.mypaldotrip.domain.user.store.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {}
