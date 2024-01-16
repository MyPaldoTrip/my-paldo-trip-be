package com.b6.mypaldotrip.domain.city.s3.repository;

import com.b6.mypaldotrip.domain.city.s3.entity.S3Entity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3Repository extends JpaRepository<S3Entity, Long> {}
