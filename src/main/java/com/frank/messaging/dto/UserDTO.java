package com.frank.messaging.dto;


import java.util.Date;

import com.frank.messaging.enumeration.Gender;
import lombok.Data;

@Data
public class UserDTO { // data transfer object
    private Integer id;
    private String username;
    private String nickname;
    private String password;
    private String loginToken;
    private Date registerTime;
    private Date lastLoginTime;
    private Gender gender;
    private String email;
    private String address;
    private Boolean isValid;

}
