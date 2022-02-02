package it.training.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api")
public class MyAPI {

    @Autowired
    CustomUserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/public/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/auth/info")
    public Authentication authInfo() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    //@RolesAllowed({"ADMIN"})
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/auth/createUser/{username}")
    public CustomUser createUser(@PathVariable("username") String username, @RequestParam("password") String password) {
        CustomUser cu = new CustomUser();
        cu.setUserName(username);
        cu.setPassword(passwordEncoder.encode(password));
        System.out.println("Saving user: "+username+", with password: "+password);
        repository.save(cu);

        return cu;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name == #username")
    @GetMapping("/auth/changePassword/{username}")
    public ResponseEntity<CustomUser> changePassword(@PathVariable("username") String username, @RequestParam("password") String password) {
        CustomUser cu;
        if (repository.existsById(username)) {
            cu = repository.findById(username).get();
            cu.setPassword(password);
            repository.save(cu);
            return new ResponseEntity<CustomUser>(cu, HttpStatus.OK);
        } else return new ResponseEntity<CustomUser>(HttpStatus.NOT_FOUND);
    }
}
