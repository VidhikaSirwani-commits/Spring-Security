package com.codemine.example.controller;

import com.codemine.example.entity.Users;
import com.codemine.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users user){
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user){
//        System.out.println(user);
//        return "Success";
        // i want to verfiy the user then return success simply i do not want success

        return userService.verify(user);
    }

}
