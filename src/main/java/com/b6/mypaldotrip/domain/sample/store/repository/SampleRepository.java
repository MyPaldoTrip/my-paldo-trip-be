package com.b6.mypaldotrip.domain.sample.store.repository;

import com.b6.mypaldotrip.domain.sample.store.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<SampleEntity, Long> {

}
