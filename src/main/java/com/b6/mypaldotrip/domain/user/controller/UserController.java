package com.b6.mypaldotrip.domain.user.controller;

import com.b6.mypaldotrip.domain.user.controller.dto.request.UserSignUpReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserUpdateReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserDeleteRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserGetProfileRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserSignUpRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserUpdateRes;
import com.b6.mypaldotrip.domain.user.service.UserService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${mpt.version}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final VersionConfig versionConfig;

    @PostMapping("/signup")
    public ResponseEntity<RestResponse<UserSignUpRes>> signup(
            @Valid @RequestBody UserSignUpReq req) {
        UserSignUpRes res = userService.signup(req);
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<RestResponse<UserDeleteRes>> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserDeleteRes res = userService.deleteUser(userDetails.getUserEntity().getUserId());
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<RestResponse<UserGetProfileRes>> viewProfile(@PathVariable Long userId) {
        UserGetProfileRes res = userService.viewProfile(userId);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PutMapping
    public ResponseEntity<RestResponse<UserUpdateRes>> updateProfile(
            @RequestBody UserUpdateReq req, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserUpdateRes res = userService.updateProfile(req, userDetails.getUserEntity().getUserId());
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
