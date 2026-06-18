package com.example.system_design_java_project1.service;

import com.example.system_design_java_project1.util.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    @Autowired
    private RedisService redisService;

    @Value("${app.otp.expiry-minutes:5}")
    private long otpExpiryMinutes;

    @Value("${app.otp.length:6}")
    private int otpLength;

    public String generateAndSendOtp(String email){
        String otp = OtpGenerator.generateOtp(otpLength);
        String key = "otp:" + email;

        //Store OTP in Redis with Expiry
        redisService.setWithExpiry(key, otp, otpExpiryMinutes, TimeUnit.MINUTES);

        System.out.println("OTP for " + email + ": " + otp);

        return otp;
    }

    public boolean verifyOtp(String email, String otp){
        String key = "otp:" + email;
        String storedOtp = redisService.get(key);

        if(storedOtp == null)
            return false;

        boolean isValid = storedOtp.equals(otp);

        if(isValid){
            //Delete OTP after successful verification
            redisService.delete(key);
        }

        return isValid;
    }

    public long getOtpExpiryTime(String email) {
        String key = "otp:" + email;
        long ttl = redisService.getExpiry(key, TimeUnit.SECONDS);
        return ttl < 0 ? 0 : ttl;
    }

    public boolean isOtpExists(String email) {
        String key = "otp:" + email;
        return redisService.exists(key);
    }
}
