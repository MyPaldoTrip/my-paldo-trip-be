package com.b6.mypaldotrip.domain.user.service;

import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationCheckReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationSubmitReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationConfirmRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationSubmitRes;
import com.b6.mypaldotrip.domain.user.exception.ApplicationErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.ApplicationEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.repository.ApplicationRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;

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

    @Transactional
    public ApplicationConfirmRes confirm(ApplicationCheckReq req) {
        ApplicationEntity applicationEntity = findApplication(req);
        applicationEntity.confirm();
        UserEntity userEntity = userService.findUser(applicationEntity.getUserEntity().getUserId());
        if (req.accept() == null) {
            userService.rejectApplication(userEntity);
            return ApplicationConfirmRes.builder()
                    .applicationId(applicationEntity.getApplicationId())
                    .email(userEntity.getEmail())
                    .message("신청이 거절 되었습니다.")
                    .build();
        }
        userService.acceptApplication(userEntity);
        return ApplicationConfirmRes.builder()
                .applicationId(applicationEntity.getApplicationId())
                .email(userEntity.getEmail())
                .message("해당 신청이 승인 되었습니다.")
                .build();
    }

    private ApplicationEntity findApplication(ApplicationCheckReq req) {
        return applicationRepository
                .findById(req.applicationId())
                .orElseThrow(() -> new GlobalException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
    }
}
