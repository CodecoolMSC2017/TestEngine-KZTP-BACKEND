package com.kztp.testengine.controller;

import com.kztp.testengine.exception.UserNotActivatedException;
import com.kztp.testengine.model.User;
import com.kztp.testengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public User get(Principal principal) throws UserNotActivatedException {
        User user =userService.getUserByUsername(principal.getName());
        if (!user.getUserToken().isActivated()) {
            throw new UserNotActivatedException("User not activated.");
        }
        return userService.getUserByUsername(principal.getName());
    }

    @DeleteMapping("")
    public void delete(HttpSession session) {
        session.invalidate();
    }
}
