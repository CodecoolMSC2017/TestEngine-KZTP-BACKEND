package com.kztp.testengine.controller;

import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.model.User;
import com.kztp.testengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User add(@RequestBody Map<String,String> map) throws MessagingException {
        String email = map.get("email");
        String username = map.get("username");
        String password = map.get("password");
        String confirmationPassword = map.get("confirmationPassword");
        return userService.createUser(email,username,password,confirmationPassword);
    }

    @PostMapping("/admin/newadmin")
    public User addAdmin(@RequestBody Map<String,String> map) throws UnauthorizedRequestException {
        String email = map.get("email");
        String username = map.get("username");
        String password = map.get("password");
        String confirmationPassword = map.get("confirmationPassword");
        return userService.createAdmin(email,username,password,confirmationPassword);
    }
    @GetMapping("/register/activate")
    public void activateUser(@RequestParam("token") String token){
        userService.activateUser(token);
    }
}
