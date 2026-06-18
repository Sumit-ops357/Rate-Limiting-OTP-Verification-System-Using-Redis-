package com.example.system_design_java_project1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok reduces the boilerplate
//Using Lombok annotations getters,setters and constructors can be created automatically

//@Data :-  Used for getters ,setters and many more

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {

    private String email;
}
