package com.frank.messaging.aspect;

import com.frank.messaging.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
public class AuthenticationAspect {

    @Autowired private UserService userService;

    @Before("execution(* com.frank.messaging.controller.*.*(..))")
    public void authenticate() {

    }
}
