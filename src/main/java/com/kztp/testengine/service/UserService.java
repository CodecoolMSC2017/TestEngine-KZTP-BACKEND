package com.kztp.testengine.service;

import com.kztp.testengine.model.User;
import com.kztp.testengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

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

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void createUser(String email,String username,String password) {
        if (isEmailTaken(email)) {
            throw new IllegalArgumentException("This email address already exists, please choose another one!");
        }

        if (email.equals("") || password.equals("")) {
            throw new IllegalArgumentException("Fill out each inputs!");
        }

        if (userDetailsManager.userExists(username)) {
            throw new IllegalArgumentException("This username is already taken,please choose another one.");
        }

        userDetailsManager.createUser(new org.springframework.security.core.userdetails.User(
                email,
                pwEncoder.encode(password),
                AuthorityUtils.createAuthorityList("USER_ROLE")));
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


}
