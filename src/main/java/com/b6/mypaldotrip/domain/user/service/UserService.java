package com.b6.mypaldotrip.domain.user.service;

import com.b6.mypaldotrip.domain.follow.controller.dto.response.FollowRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserListReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserSignUpReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserUpdateReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserDeleteRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserGetProfileRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserSignUpRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserUpdateRes;
import com.b6.mypaldotrip.domain.user.exception.UserErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
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

    // 지금 레코드 써서 좀 많이 지저분한데 레코드 쓰면서도 해결 할 방법이 있을까요?
    @Transactional
    public UserGetProfileRes viewProfile(Long userId) {
        UserEntity userEntity = findUser(userId);
        userRepository.findByIdFetchFollower(userId);
        userRepository.findByIdFetchFollowing(userId);
        userRepository.findByIdFetchReview(userId);
        return UserGetProfileRes.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .introduction(userEntity.getIntroduction())
                .profileURL(userEntity.getFileURL())
                .age(userEntity.getAge())
                .level(userEntity.getLevel())
                .reviewListResList(
                        userEntity.getReviewList().stream()
                                .map(
                                        reviewEntity ->
                                                ReviewListRes.builder()
                                                        .reviewId(reviewEntity.getReviewId())
                                                        .content(reviewEntity.getContent())
                                                        .score(reviewEntity.getScore())
                                                        .modifiedAt(reviewEntity.getModifiedAt())
                                                        .build())
                                .toList())
                .followingEntityList(
                        userEntity.getFollowingList().stream()
                                .map(
                                        followingEntity ->
                                                FollowRes.builder()
                                                        .userId(
                                                                followingEntity
                                                                        .getFollowingUser()
                                                                        .getUserId())
                                                        .username(
                                                                followingEntity
                                                                        .getFollowingUser()
                                                                        .getUsername())
                                                        .email(
                                                                followingEntity
                                                                        .getFollowingUser()
                                                                        .getEmail())
                                                        .build())
                                .toList())
                .followerEntityList(
                        userEntity.getFollowerList().stream()
                                .map(
                                        followerEntity ->
                                                FollowRes.builder()
                                                        .userId(
                                                                followerEntity
                                                                        .getFollowedUser()
                                                                        .getUserId())
                                                        .username(
                                                                followerEntity
                                                                        .getFollowedUser()
                                                                        .getUsername())
                                                        .email(
                                                                followerEntity
                                                                        .getFollowedUser()
                                                                        .getEmail())
                                                        .build())
                                .toList())
                .build();
    }

    @Transactional
    public UserUpdateRes updateProfile(MultipartFile multipartFile, String reqJson, Long userId)
            throws IOException {
        UserEntity userEntity = findUser(userId);

        ObjectMapper objectMapper = new ObjectMapper();
        UserUpdateReq req = objectMapper.readValue(reqJson, UserUpdateReq.class);
        if (req.username()==null || req.password()==null){
            throw new GlobalException(GlobalResultCode.VALIDATION_ERROR);
        }
        String password = passwordEncoder.encode(req.password());
        String fileUrl = null;
        if(multipartFile!=null){
            try {
                fileUrl = s3Provider.updateFile(userEntity, multipartFile);
            } catch (GlobalException e) {
                fileUrl = s3Provider.saveFile(multipartFile, "user");
            }    
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

    @Transactional
    public List<UserListRes> getUserList(UserListReq req, UserDetailsImpl userDetails) {

        List<UserEntity> userEntityList = userRepository.findByDynamicConditions(req, userDetails);
        userRepository.fetchFollowerList(req, userDetails);

        return userEntityList.stream()
                .map(
                        userEntity ->
                                UserListRes.builder()
                                        .userId(userEntity.getUserId())
                                        .email(userEntity.getEmail())
                                        .username(userEntity.getUsername())
                                        .age(userEntity.getAge())
                                        .level(userEntity.getLevel())
                                        .modified(
                                                String.valueOf(userEntity.getModifiedAt())
                                                        .substring(0, 10))
                                        .userRoleValue(userEntity.getUserRole().getValue())
                                        .writeReviewCnt(userEntity.getReviewList().size())
                                        .followerCnt(userEntity.getFollowerList().size())
                                        .build())
                .toList();
    }

    public void acceptApplication(UserEntity userEntity) {
        userEntity.acceptPermission();
    }

    public void rejectApplication(UserEntity userEntity) {
        userEntity.reject();
    }

    public UserGetProfileRes viewMyProfile(Long userId) {
        return UserGetProfileRes.builder()
                .userId(userId)
                .username(userRepository.findUsernameByUserId(userId))
                .build();
    }
}
