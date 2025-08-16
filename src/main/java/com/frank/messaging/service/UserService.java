package com.frank.messaging.service;

import java.util.Date;
import java.util.regex.Pattern;

import com.frank.messaging.dao.UserDAO;
import com.frank.messaging.dto.UserDTO;
import com.frank.messaging.enumeration.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService { // UserLogic, UserManager

    @Autowired private UserDAO userDAO;

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public void register(String username,
                         String password,
                         String repeatPassword,
                         String email,
                         String nickname,
                         String address,
                         Gender gender) throws Exception {
        System.out.println(email);
        // input validation
        if (!password.equals(repeatPassword)) {
            throw new Exception("Passwords are different.");
        }

        if (username == null || username.isEmpty() || nickname == null || nickname.isEmpty()) {
            throw new Exception("Username or nickname is empty");
        }

        if (!isValidEmail(email)) {
            throw new Exception("Email is not valid.");
        }

        // store the user information to a DB
        UserDTO userDTO = new UserDTO();

        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setEmail(email);
        userDTO.setNickname(nickname);
        userDTO.setAddress(address);
        userDTO.setGender(gender);
        userDTO.setRegisterTime(new Date());

        this.userDAO.insert(userDTO);
    }


    private static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
