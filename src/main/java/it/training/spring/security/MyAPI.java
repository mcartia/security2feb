package it.training.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyAPI {

    @GetMapping("/public/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/auth/info")
    public Authentication authInfo() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
