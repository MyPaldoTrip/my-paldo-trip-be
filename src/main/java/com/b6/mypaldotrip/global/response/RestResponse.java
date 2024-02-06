package com.b6.mypaldotrip.global.response;

import com.b6.mypaldotrip.global.template.ResultCode;
import java.io.Serializable;
import java.util.ArrayList;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class RestResponse<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;
    //    private MetaData metaData;

    public ResponseEntity<RestResponse<T>> toResponseEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(this, headers, HttpStatus.valueOf(code));
    }

    public static <T> RestResponse<T> success(T data, ResultCode resultCode, String version) {
        return RestResponse.<T>builder()
                .code(resultCode.getHttpStatus().value())
                .message(resultCode.getMessage())
                .data(data)
                //                .metaData(MetaData.builder().apiVersion(version).build())
                .build();
    }

    public static <T> RestResponse<T> error(ResultCode resultCode, String version) {
        return RestResponse.<T>builder()
                .code(resultCode.getHttpStatus().value())
                .message(resultCode.getMessage())
                //                .metaData(MetaData.builder().apiVersion(version).build())
                .build();
    }

    public static RestResponse<Object> argumentValidException(
            ArrayList<String> errors, String version) {
        return RestResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("requestValidException : " + errors.stream().toList().toString())
                //                .metaData(MetaData.builder().apiVersion(version).build())
                .build();
    }
}
