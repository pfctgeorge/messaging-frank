package com.frank.messaging.service;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import com.frank.messaging.dao.UserDAO;
import com.frank.messaging.dao.UserValidationCodeDAO;
import com.frank.messaging.dto.UserDTO;
import com.frank.messaging.dto.UserValidationCodeDTO;
import com.frank.messaging.enumeration.Gender;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserService { // UserLogic, UserManager

    private Duration LOGIN_TOKEN_EXPIRY = Duration.ofDays(14);
    @Autowired private UserDAO userDAO;
    @Autowired private UserValidationCodeDAO userValidationCodeDAO;

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
        userDTO.setEmail(email); // 2FA = second factor authentication
        userDTO.setNickname(nickname);
        userDTO.setAddress(address);
        userDTO.setGender(gender);
        userDTO.setRegisterTime(new Date());
        userDTO.setIsValid(false);

        this.userDAO.insert(userDTO);

        String validationCode = String.format("%06d", new Random().nextInt(1000000));
        UserValidationCodeDTO userValidationCodeDTO = new UserValidationCodeDTO();
        userValidationCodeDTO.setUserId(userDTO.getId());
        userValidationCodeDTO.setValidationCode(validationCode);

        this.userValidationCodeDAO.insert(userValidationCodeDTO);

        String subject = "Validation Code for User Registration";
        String content = "Validation code: " + validationCode;
        //      this.emailService.send(email, subject, content);
    }


    private static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public void activate(String username, String validationCode) throws Exception {
        UserDTO userDTO = this.userDAO.selectByUsername(username);
        if (userDTO == null) {
            throw new Exception("User does not exist");
        }
        UserValidationCodeDTO userValidationCodeDTO = this.userValidationCodeDAO.selectByUserId(userDTO.getId());
        if (!validationCode.equals(userValidationCodeDTO.getValidationCode())) {
            throw new Exception("Wrong user validation code");
        }

        this.userDAO.updateToValid(userDTO.getId());
        this.userValidationCodeDAO.deleteById(userValidationCodeDTO.getId());

    }

    public UserDTO authenticate(String loginToken) throws Exception {
        UserDTO userDTO = this.userDAO.selectByLoginToken(loginToken);
        if (userDTO == null) {
            throw new Exception("Login token is not valid");
        }
        if (System.currentTimeMillis() - userDTO.getLastLoginTime().getTime() > LOGIN_TOKEN_EXPIRY.toMillis()) {
            throw new Exception("Login token expired");
        }
        return userDTO;
    }

    public String login(String username, String password) throws Exception {
        UserDTO userDTO = this.userDAO.selectByUsername(username);
        if (userDTO == null) {
            throw new Exception("User does not exist");
        }
        if (!password.equals(userDTO.getPassword())) {
            throw new Exception("Wrong password");
        }

        String loginToken = RandomStringUtils.secure().nextAlphanumeric(64);
        Date lastLoginTime = new Date();
        this.userDAO.login(loginToken, lastLoginTime, userDTO.getId());
        return loginToken;
    }

    public void logout(String loginToken) throws Exception {

        UserDTO userDTO = this.userDAO.selectByLoginToken(loginToken);
        if (userDTO == null) {
            throw new Exception("Login token is not valid");
        }
        this.userDAO.logout(userDTO.getId());

    }
}
