package com.kztp.testengine.service;

import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.exception.UserException;
import com.kztp.testengine.model.User;
import com.kztp.testengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import javax.naming.InvalidNameException;

@Component
public final class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder pwEncoder;

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) throws IllegalArgumentException {
       /* if (!userDetailsManager.userExists(username)) {
            throw new IllegalArgumentException("There is no user with this name.");
        }
        return userRepository.findByUsername(username);*/
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException(username + " does not found!");
        }

        return user;
    }

    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User createUser(String email,String username,String password,String confirmationPassword) {
        if(isEveryInputValid(email,username,password,confirmationPassword)) {
            userDetailsManager.createUser(new org.springframework.security.core.userdetails.User(
                    username,
                    pwEncoder.encode(password),
                    AuthorityUtils.createAuthorityList("ROLE_USER")));
        }
        User user = userRepository.findByUsername(username);
        user.setEmail(email);
        user.setRank("newbie");
        userRepository.save(user);
        return user;
    }

    public User createAdmin(String username,String email,String password,String confirmationPassword) throws UnauthorizedRequestException {
        User currentUser = getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currentUser.getAuthorities().contains("ROLE_ADMIN")) {
            if (isEveryInputValid(email, username, password, confirmationPassword)) {
                userDetailsManager.createUser(new org.springframework.security.core.userdetails.User(
                        username,
                        pwEncoder.encode(password),
                        AuthorityUtils.createAuthorityList("ADMIN_ROLE")));
            }
            User user = userRepository.findByUsername(username);
            user.setEmail(email);
            user.setRank("elite");
            userRepository.save(user);
        }else {
            throw new UnauthorizedRequestException("You don't have the authority for this action.");
        }
        return userRepository.findByUsername(username);
    }
    public void changePassword(int userId,String oldPassword, String newPassword) {
        User user = userRepository.findById(userId);
        if (oldPassword.equals(pwEncoder.encode(oldPassword))) {
            userDetailsManager.changePassword(oldPassword,pwEncoder.encode(newPassword));
        }
        userRepository.save(user);
    }

    private boolean isEmailTaken(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return false;
        }

        return true;
    }


    public boolean isAdmin(int id) {
        User user = userRepository.findById(id);
        if (user.getAuthorities().contains("ADMIN_ROLE")) {
            return true;
        }

        return false;
    }


    private boolean isEveryInputValid(String email,String username,String password,String confirmationPassword) throws IllegalArgumentException {
        if(password.length() < 8 ) {
            throw new IllegalArgumentException("Password is too short.Enter minimum 8 characters");
        }

        if(email.length() == 0 || !email.contains("@")) {
            throw new IllegalArgumentException("Email is null or invalid.");
        }

        if (isEmailTaken(email)) {
            throw new IllegalArgumentException("This email address already exists, please choose another one!");
        }

        if (email.equals("") || password.equals("")) {
            throw new IllegalArgumentException("Fill out each inputs!");
        }

        if (userDetailsManager.userExists(username)) {
            throw new IllegalArgumentException("This username is already taken,please choose another one.");
        }

        if (!password.equals(confirmationPassword)) {
            throw new IllegalArgumentException("Password and confirmation password doesn't match.");
        }
        return true;
    }

    public void rankUpUser(User user) throws UserException {
        if(user.getRank().equals("elite")) {
            throw new UserException("Your rank is already elite,can't get higher rank.");
        }
        if(user.getRank().equals("user")) {
            user.setRank("elite");
            userRepository.save(user);
        }
        if (user.getRank().equals("newbie")) {
            user.setRank("user");
            userRepository.save(user);
        }

    }

    public User getLoggedUser(){
        return getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }


}
