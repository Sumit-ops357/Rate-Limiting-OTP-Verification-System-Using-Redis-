package com.example.system_design_java_project1.model;

//Lombok reduces the boilerplate
//Using Lombok annotations getters,setters and constructors can be created automatically

//@Data :-  Used for getters ,setters and many more

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerifyRequest {
    private String email;
    private String otp;
}
