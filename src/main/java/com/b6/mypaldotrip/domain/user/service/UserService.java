package com.b6.mypaldotrip.domain.user.service;

import com.b6.mypaldotrip.domain.user.controller.dto.request.UserSignUpReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserSignUpRes;
import com.b6.mypaldotrip.domain.user.exception.UserErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.User;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND_USER_BY_USERID));
    }

    public UserSignUpRes signup(UserSignUpReq req) {
        String password = passwordEncoder.encode(req.password());
        User user =
                User.builder()
                        .email(req.email())
                        .username(req.username())
                        .password(password)
                        .build();
        userRepository.save(user);
        return UserSignUpRes.builder().email(user.getEmail()).username(user.getUsername()).build();
    }
}
