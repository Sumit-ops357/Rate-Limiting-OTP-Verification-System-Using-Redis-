package com.example.system_design_java_project1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Lombok reduces the boilerplate
//Using Lombok annotations getters,setters and constructors can be created automatically

//@Data :-  Used for getters ,setters and many more

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private int statusCode;

    public ApiResponse(boolean b, String anUnexpectedErrorOccurred, String message, int i) {
    }
}
