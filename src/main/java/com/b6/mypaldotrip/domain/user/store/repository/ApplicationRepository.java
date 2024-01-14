package com.b6.mypaldotrip.domain.user.store.repository;

import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetRes;
import com.b6.mypaldotrip.domain.user.store.entity.ApplicationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    @Query(
            "select NEW com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetListRes(a.applicationId,a.userEntity.email,a.userEntity.username,a.title,a.verified) "
                    + "from ApplicationEntity a "
                    + "where a.verified =false ")
    List<ApplicationGetListRes> findByVerifiedIsFalse();

    @Query(
            "select NEW com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetRes(a.applicationId,a.userEntity.email,a.userEntity.username,a.title,a.content,a.verified) "
                    + "from ApplicationEntity a "
                    + "where a.applicationId =:applicationId")
    ApplicationGetRes findApplication(Long applicationId);
}
