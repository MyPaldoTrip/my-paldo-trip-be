package com.b6.mypaldotrip.domain.user.controller;

import com.b6.mypaldotrip.domain.user.controller.dto.request.UserListReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserSignUpReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserDeleteRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserGetMyProfileRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserGetProfileRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserSignUpRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserUpdateRes;
import com.b6.mypaldotrip.domain.user.service.UserService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping
    public ResponseEntity<RestResponse<UserGetMyProfileRes>> viewMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserGetMyProfileRes res =
                userService.viewMyProfile(userDetails.getUserEntity().getUserId());
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PutMapping
    public ResponseEntity<RestResponse<UserUpdateRes>> updateProfile(
            @RequestPart(required = false) MultipartFile multipartFile,
            @RequestPart String req,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws IOException {
        UserUpdateRes res =
                userService.updateProfile(
                        multipartFile, req, userDetails.getUserEntity().getUserId());
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<RestResponse<List<UserListRes>>> getUserList(
            @RequestBody UserListReq req, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<UserListRes> res = userService.getUserList(req, userDetails);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
