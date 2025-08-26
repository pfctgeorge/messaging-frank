package com.frank.messaging.controller;

import com.frank.messaging.request.ActivateUserRequest;
import com.frank.messaging.request.RegisterUserRequest;
import com.frank.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void login() {

    }
}
