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
import com.b6.mypaldotrip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignUpRes signup(UserSignUpReq req) {
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
        return UserDeleteRes.builder().build();
    }

    public UserGetProfileRes viewProfile(Long userId) {
        UserEntity userEntity = findUser(userId);
        return UserGetProfileRes.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .introduction(userEntity.getIntroduction())
                .profileURL(userEntity.getProfileURL())
                .age(userEntity.getAge())
                .level(userEntity.getLevel())
                .build();
    }

    @Transactional
    public UserUpdateRes updateProfile(UserUpdateReq req, Long userId) {
        UserEntity userEntity = findUser(userId);
        String password = passwordEncoder.encode(req.password());

        userEntity.update(req.username(), req.introduction(), req.age(), password);

        return UserUpdateRes.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .introduction(userEntity.getIntroduction())
                .profileURL(userEntity.getProfileURL())
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
