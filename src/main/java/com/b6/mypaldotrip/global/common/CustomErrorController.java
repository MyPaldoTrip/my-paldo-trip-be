package com.b6.mypaldotrip.global.common;

import com.b6.mypaldotrip.global.response.RestResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @Value("${mpt.version}")
    private String version;

    @RequestMapping("/error")
    public ResponseEntity<RestResponse<Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                RestResponse<Object> errorResponse =
                        RestResponse.error(GlobalResultCode.NOT_FOUND_URL, version);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
