package com.b6.mypaldotrip.domain.user.service;

import com.b6.mypaldotrip.domain.user.controller.dto.request.UserSignUpReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserUpdateReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserDeleteRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserGetProfileRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserSignUpRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserUpdateRes;
import com.b6.mypaldotrip.domain.user.exception.UserErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Provider s3Provider;
    private final EmailAuthService emailAuthService;

    public UserSignUpRes signup(UserSignUpReq req) {
        if (!emailAuthService.isEmailVerified(req.email())) {
            log.error("해당 이메일 검증 false");
            throw new GlobalException(UserErrorCode.BEFORE_EMAIL_VALIDATION);
        }
        String password = passwordEncoder.encode(req.password());

        UserEntity userEntity =
                UserEntity.builder()
                        .email(req.email())
                        .username(req.username())
                        .password(password)
                        .build();
        userRepository.save(userEntity);
        return UserSignUpRes.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .build();
    }

    public UserDeleteRes deleteUser(Long userId) {
        UserEntity userEntity = findUser(userId);
        userRepository.delete(userEntity);
        try {
            s3Provider.deleteFile(userEntity);
        } catch (GlobalException e) {
            return UserDeleteRes.builder().message("유저 삭제, 삭제할 파일 없음").build();
        }
        return UserDeleteRes.builder().message("유저, 유저파일 삭제").build();
    }

    public UserGetProfileRes viewProfile(Long userId) {
        UserEntity userEntity = findUser(userId);
        return UserGetProfileRes.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .introduction(userEntity.getIntroduction())
                .profileURL(userEntity.getFileURL())
                .age(userEntity.getAge())
                .level(userEntity.getLevel())
                .build();
    }

    @Transactional
    public UserUpdateRes updateProfile(MultipartFile multipartFile, UserUpdateReq req, Long userId)
            throws IOException {
        UserEntity userEntity = findUser(userId);
        String password = passwordEncoder.encode(req.password());
        String fileUrl;
        try {
            fileUrl = s3Provider.updateFile(userEntity, multipartFile);
        } catch (GlobalException e) {
            fileUrl = s3Provider.saveFile(multipartFile, "user");
        }

        userEntity.update(req.username(), req.introduction(), req.age(), password, fileUrl);

        return UserUpdateRes.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .introduction(userEntity.getIntroduction())
                .fileURL(fileUrl)
                .age(userEntity.getAge())
                .level(userEntity.getLevel())
                .build();
    }

    public UserEntity findUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND_USER_BY_USERID));
    }
}
