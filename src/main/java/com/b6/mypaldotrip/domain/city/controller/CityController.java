package com.b6.mypaldotrip.domain.city.controller;

import com.b6.mypaldotrip.domain.city.controller.dto.request.CityCreateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.CityUpdateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityCreateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityDeleteRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityListRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityUpdateRes;
import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;
    private final VersionConfig versionConfig;

    @PostMapping//생성
    public ResponseEntity<RestResponse<CityCreateRes>> createCity(
        @RequestBody CityCreateReq cityCreateReq
        //@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CityCreateRes res = cityService.createCity(cityCreateReq);
        //userDetails.getUser());
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
            .toResponseEntity();

    }

    @PutMapping("/{cityId}")//수정
    public ResponseEntity<RestResponse<CityUpdateRes>> updateCity(
        @PathVariable Long cityId,
        @RequestBody CityUpdateReq cityUpdateReq
        //@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            CityUpdateRes res = cityService.updateCity(cityId, cityUpdateReq);
            return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
        } catch (DataIntegrityViolationException e) {
            // 중복 예외 처리
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(RestResponse.<CityUpdateRes>error(GlobalResultCode.DUPLICATE,
                        versionConfig.getVersion())
                    .toResponseEntity().getBody());
        }

    }


    @DeleteMapping("/{cityId}")//삭제
    public ResponseEntity<RestResponse<CityDeleteRes>> deleteCity(@PathVariable Long cityId
        //@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CityDeleteRes res = cityService.deleteCity(cityId);
        //userDetails.getUser());
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
            .toResponseEntity();

    }

    @GetMapping("/provinces")//중복을 제거한 도 전체 조회
    public ResponseEntity<RestResponse<List<CityEntity>>> getProvinceList() {
        List<CityEntity> res = cityService.getProvinceList();

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
            .toResponseEntity();
    }

    @GetMapping("/provinces/{cityName}")//시 전체 조회
    public ResponseEntity<RestResponse<List<CityListRes>>> getCityList(
        @PathVariable String cityName) {
        List<CityListRes> res = cityService.getCityList(cityName);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
            .toResponseEntity();
    }

}
