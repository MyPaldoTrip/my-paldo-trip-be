package com.b6.mypaldotrip.domain.user.store.repository;

import com.b6.mypaldotrip.domain.user.store.entity.EmailAuth;
import org.springframework.data.repository.CrudRepository;

public interface EmailAuthRepository extends CrudRepository<EmailAuth, String> {}
