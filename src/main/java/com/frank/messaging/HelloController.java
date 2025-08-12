package com.frank.messaging;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(@RequestParam(required = false, defaultValue = "Alice") String name) {
        return "Hello world, " + name + "!";
    }
}
