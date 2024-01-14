package com.b6.mypaldotrip.domain.user.service;

import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationSubmitReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationSubmitRes;
import com.b6.mypaldotrip.domain.user.store.entity.ApplicationEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.repository.ApplicationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationSubmitRes submit(ApplicationSubmitReq req, UserEntity userEntity) {
        ApplicationEntity applicationEntity =
                ApplicationEntity.builder()
                        .userEntity(userEntity)
                        .title(req.title())
                        .content(req.content())
                        .build();
        applicationRepository.save(applicationEntity);
        return ApplicationSubmitRes.builder()
                .email(applicationEntity.getUserEntity().getEmail())
                .username(applicationEntity.getUserEntity().getUsername())
                .title(applicationEntity.getTitle())
                .content(applicationEntity.getContent())
                .build();
    }

    public List<ApplicationGetListRes> getList() {
        return applicationRepository.findByVerifiedIsFalse();
    }

    public ApplicationGetRes getApplication(Long applicationId) {
        return applicationRepository.findApplication(applicationId);
    }
}
