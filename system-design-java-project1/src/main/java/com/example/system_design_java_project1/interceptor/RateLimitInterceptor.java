package com.example.system_design_java_project1.interceptor;

import com.example.system_design_java_project1.exception.RateLimitExceededException;
import com.example.system_design_java_project1.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitService rateLimitService;

    private String getClientIp(HttpServletRequest request){
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handler) throws Exception {

        String clientIp = getClientIp(request);

        //Check if the request is allowed
        if(!rateLimitService.isAllowed(clientIp)){
            long resetTime = rateLimitService.getResetTime(clientIp);

            throw new RateLimitExceededException("Rate limit exceeded ,Max 5 requests per minute", resetTime);
        }

        //Add rate limit info to response headers
        long remaining = rateLimitService.getRemainingRequests(clientIp);
        long resetTime = rateLimitService.getResetTime(clientIp);

        response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
        response.setHeader("X-RateLimit-Reset", String.valueOf(resetTime));

        //Returning true :-Means the request is allowed ,now u can proceed to the controller
        return true;
    }
}
