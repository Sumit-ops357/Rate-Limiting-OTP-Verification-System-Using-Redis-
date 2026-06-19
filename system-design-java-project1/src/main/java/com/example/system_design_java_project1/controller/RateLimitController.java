package com.example.system_design_java_project1.controller;

import com.example.system_design_java_project1.model.ApiResponse;
import com.example.system_design_java_project1.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class RateLimitController {

    @Autowired
    private RateLimitService rateLimitService;

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<Map<String, Object>>> ping(HttpServletRequest request){

        String clientIp = getClientIp(request);
        long remaining = rateLimitService.getRemainingRequests(clientIp);
        long resetTime = rateLimitService.getResetTime(clientIp);

        Map<String, Object> data = new HashMap<>();
        data.put("message", "pong");
        data.put("clientIp", clientIp);
        data.put("remaining", remaining);
        data.put("resetTime", resetTime);

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                true,
                "Request successful",
                data,
                200
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health(){
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Service is Healthy",
                "OK",
                200
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
