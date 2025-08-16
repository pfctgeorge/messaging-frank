package com.frank.messaging;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(@RequestParam(required = false, defaultValue = "Alice") String name,
                        @RequestHeader("User-Agent") String userAgent) {
        return "Hello world, " + name + ", user-agent: " + userAgent + "!";
    }

    @PostMapping("/")
    public User helloPost(@RequestBody User user) { // de-serialization: string/bytearray -> object in memory
        // User user = new User();
        // user.setUsername("George");
        // user.set
        user.setUsername(user.getUsername() + "?");
        return user; // serialization: object in memory -> string/bytearray
    }

}
