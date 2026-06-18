package com.example.system_design_java_project1.exception;

import lombok.Getter;

//By extending RuntimeException, I can customize the exceptions like i have created my own RateLimitExceededException
//With a message :- "Too many requests"

//Like :-  ArrayOutOfBoundary exception, Null pointer exception and many more

@Getter
public class RateLimitExceededException extends RuntimeException{

    private long retryAfter;

    public RateLimitExceededException(String message, long retryAfter){
        super(message);   //Message saying:- "Too many requests"
        this.retryAfter = retryAfter;
    }
}
