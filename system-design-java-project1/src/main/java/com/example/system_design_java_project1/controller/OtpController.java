package com.example.system_design_java_project1.controller;

import com.example.system_design_java_project1.exception.InvalidOtpException;
import com.example.system_design_java_project1.model.ApiResponse;
import com.example.system_design_java_project1.model.OtpRequest;
import com.example.system_design_java_project1.model.OtpVerifyRequest;
import com.example.system_design_java_project1.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateOtp(@RequestBody OtpRequest request){

        if(request.getEmail() == null || request.getEmail().trim().isEmpty()){
            throw new InvalidOtpException("Email is required");
        }

        try{

            String otp = otpService.generateAndSendOtp(request.getEmail());

            Map<String, Object> data = new HashMap<>();
            data.put("email", request.getEmail());
            data.put("message", "OTP sent successfully");
            data.put("expiryMinutes", 5);

            ApiResponse<Map<String,Object>> response = new ApiResponse<>(
                    true,
                    "OTP generated Successfully",
                    data,
                    200
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e){
              throw new InvalidOtpException("Failed to generate OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyOtp(@RequestBody OtpVerifyRequest request){

        if(request.getEmail() == null || request.getEmail().trim().isEmpty()){
            throw new InvalidOtpException("Email is required");
        }

        if(request.getOtp() == null || request.getOtp().trim().isEmpty()){
            throw new InvalidOtpException("OTP is required");
        }

        if(!otpService.isOtpExists(request.getEmail())){
            throw new InvalidOtpException("OTP not found or Expired for this email");
        }

        if(!otpService.verifyOtp(request.getEmail(), request.getOtp())){
            throw new InvalidOtpException("Invalid OTP");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("email", request.getEmail());
        data.put("verified", true);
        data.put("message", "OTP verified successfully");

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
          true,
          "OTP verified successfully",
          data,
          200
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status/{email}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkOtpStatus(@PathVariable String email){

        boolean exists = otpService.isOtpExists(email);
        long expiryTime = otpService.getOtpExpiryTime(email);

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("otpExists", exists);
        data.put("expirySeconds", expiryTime);

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                true,
                exists ? "OTP is valid" : "OTP not found or Expired",
                data,
                200
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
