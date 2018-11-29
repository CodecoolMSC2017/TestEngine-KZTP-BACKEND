package com.kztp.testengine.controller;

import com.kztp.testengine.model.User;
import com.kztp.testengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/change-password")
    public void changePassword(@RequestBody Map<String,String> map) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String oldPassword = map.get("oldpassword");
        String newPassword = map.get("newpassword");
        userService.changePassword(user.getId(),oldPassword,newPassword);
    }

    @GetMapping(path = "/{username}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByUsername(@PathVariable("username")String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping(path = "/mysettings")
    public User getLoggedUser(){
        return userService.getLoggedUser();
    }

}
