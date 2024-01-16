package com.b6.mypaldotrip.domain.city.controller;

import com.b6.mypaldotrip.domain.city.controller.dto.request.CityCreateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.CityUpdateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.ProvinceListReq;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityCreateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityDeleteRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityListRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityUpdateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.ProvinceListRes;
import com.b6.mypaldotrip.domain.city.s3.dto.S3Res;
import com.b6.mypaldotrip.domain.city.s3.service.S3Service;
import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;
    private final VersionConfig versionConfig;
    private final S3Service s3Service;

    @PostMapping // 생성
    public ResponseEntity<RestResponse<CityCreateRes>> createCity(
            @RequestBody CityCreateReq cityCreateReq
            // @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        CityCreateRes res = cityService.createCity(cityCreateReq);
        // userDetails.getUser());
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PutMapping("/{cityId}") // 수정
    public ResponseEntity<RestResponse<CityUpdateRes>> updateCity(
            @PathVariable Long cityId, @RequestBody CityUpdateReq cityUpdateReq
            // @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        CityUpdateRes res = cityService.updateCity(cityId, cityUpdateReq);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{cityId}") // 삭제
    public ResponseEntity<RestResponse<CityDeleteRes>> deleteCity(@PathVariable Long cityId
            // @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        CityDeleteRes res = cityService.deleteCity(cityId);
        // userDetails.getUser());
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/provinces") // 중복을 제거한 도 전체 조회
    public ResponseEntity<RestResponse<List<ProvinceListRes>>> getProvinceList() {
        List<ProvinceListRes> res = cityService.getProvinceList();

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/provinces/{provincesName}") // 시 전체 조회
    public ResponseEntity<RestResponse<List<CityListRes>>> getCityList(
            @PathVariable String provincesName) {
        List<CityListRes> res = cityService.getCityList(provincesName);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/provinces/infoSort")
    public ResponseEntity<RestResponse<List<ProvinceListRes>>> getProvinceListInfoSort(
            @RequestBody ProvinceListReq req) {
        List<ProvinceListRes> res = cityService.getProvinceListInfoSort(req);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PostMapping("/{cityId}/files") // 파일 생성
    public ResponseEntity<RestResponse<S3Res>> saveFile(
            @PathVariable Long cityId, @RequestPart(value = "file") MultipartFile multipartFile)
            throws IOException {
        S3Res res = s3Service.saveFile(cityId, multipartFile);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PutMapping("/files/{fileId}") // 파일 수정
    public ResponseEntity<RestResponse<S3Res>> updateFile(
            @PathVariable Long fileId, @RequestPart(value = "file") MultipartFile multipartFile)
            throws IOException {
        S3Res res = s3Service.updateFile(fileId, multipartFile);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/files/{fileId}") // 파일 삭제
    public ResponseEntity<RestResponse<S3Res>> deleteFile(@PathVariable Long fileId) {
        S3Res res = s3Service.deleteFile(fileId);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
