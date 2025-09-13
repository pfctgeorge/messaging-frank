package com.frank.messaging.service;

import com.frank.messaging.enumeration.Gender;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl2 extends UserService {

    @Override
    public void register(String username, String password, String repeatPassword, String email, String nickname,
                         String address, Gender gender) throws Exception {
        throw  new Exception("Not Implemented");
    }
}
