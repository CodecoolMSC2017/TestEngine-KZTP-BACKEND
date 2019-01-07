package com.kztp.testengine.controller;

import com.kztp.testengine.exception.PasswordException;
import com.kztp.testengine.exception.UserException;
import com.kztp.testengine.model.NewPassword;
import com.kztp.testengine.model.ResetPassword;
import com.kztp.testengine.model.User;
import com.kztp.testengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/change-password")
    public void changePassword(@RequestBody NewPassword newPassword) throws UserException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.changePassword(newPassword);
    }

    @GetMapping(path = "/user/{username}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByUsername(@PathVariable("username")String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping(path = "/user/mysettings")
    public User getLoggedUser(){
        return userService.getLoggedUser();
    }

    @PutMapping(path ="/resetpassword")
    public void resetPassword(@RequestBody ResetPassword resetPassword) throws PasswordException, UserException {
        userService.changePassword(resetPassword);
    }

    @PostMapping(path="/requestpasswordreset")
    public void requestPasswordReset(@RequestBody String email) throws UserException, MessagingException {
        userService.requestPasswordReset(email);
    }

    @GetMapping("/user/rank/progress")
    public int getUserProgress() {
        return userService.getUserProgress();
    }

}
