package com.example.system_design_java_project1.exception;

public class InvalidOtpException extends RuntimeException{

    public InvalidOtpException(String message){
        super(message);
    }

    public InvalidOtpException(String message, Throwable cause){
        super(message, cause);
    }
}
