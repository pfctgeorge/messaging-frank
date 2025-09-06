package com.frank.messaging.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import java.time.Duration;

import com.frank.messaging.request.ActivateUserRequest;
import com.frank.messaging.request.RegisterUserRequest;
import com.frank.messaging.request.UserLoginRequest;
import com.frank.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired private UserService userService;// = new UserService();

    @PostMapping("/register") // /users/register
    public void register(@RequestBody RegisterUserRequest registerUserRequest) throws Exception { // I/O exchange;
        // entrypoint
        System.out.println(registerUserRequest);
        this.userService.register(registerUserRequest.getUsername(),
                                  registerUserRequest.getPassword(),
                                  registerUserRequest.getRepeatPassword(),
                                  registerUserRequest.getEmail(),
                                  registerUserRequest.getNickname(),
                                  registerUserRequest.getAddress(),
                                  registerUserRequest.getGender()); // BullPointerException

    }

    @PostMapping("/activate")
    public void activate(@RequestBody ActivateUserRequest activateUserRequest) throws Exception {
        this.userService.activate(activateUserRequest.getUsername(), activateUserRequest.getValidationCode());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginRequest userLoginRequest) throws Exception {
        String loginToken = this.userService.login(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        return ResponseEntity.ok()
                .header(SET_COOKIE, ResponseCookie.from("login_token", loginToken)
                        .secure(true)
                        .path("/")
                        .maxAge(Duration.ofDays(14))
                        .build()
                        .toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("login_token") String loginToken) throws Exception {
        this.userService.logout(loginToken);
        return ResponseEntity.ok()
                .header(SET_COOKIE, ResponseCookie.from("login_token", "")
                        .secure(true)
                        .path("/")
                        .maxAge(Duration.ZERO)
                        .build()
                        .toString())
                .build();
    }

}
