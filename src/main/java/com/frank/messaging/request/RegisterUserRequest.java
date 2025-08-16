package com.frank.messaging.request;


import com.frank.messaging.enumeration.Gender;
import lombok.Data;

@Data
public class RegisterUserRequest {
    private String username;
    private String password;
    private String repeatPassword;
    private String email;
    private String nickname;
    private String address;
    private Gender gender; // "FEMALE"
}
